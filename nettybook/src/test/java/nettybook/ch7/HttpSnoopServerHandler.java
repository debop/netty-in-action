package nettybook.ch7;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Slf4j
public class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {

  private static final String CRLF = "\r\n";
  private final StringBuilder builder = new StringBuilder();

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    HttpRequest request = null;

    if (msg instanceof HttpRequest) {
      request = (HttpRequest) msg;
      handleRequest(ctx, request);
    }

    if (msg instanceof HttpContent) {
      handleContent(ctx, request, (HttpContent) msg);
    }
  }

  private void handleRequest(ChannelHandlerContext ctx, HttpRequest request) {
    if (HttpHeaders.is100ContinueExpected(request)) {
      send100Continue(ctx);
    }

    builder.setLength(0);
    builder.append("WELCOME TO THE WILD WILD WEB SERVER").append(CRLF);
    builder.append("===================================").append(CRLF);

    builder.append("VERSION: ").append(request.getProtocolVersion()).append(CRLF);
    builder.append("HOSTNAME: ").append(HttpHeaders.getHost(request, "unknown")).append(CRLF);
    builder.append("REQUEST_URI: ").append(request.getUri()).append(CRLF).append(CRLF);

    HttpHeaders headers = request.headers();
    headers.forEach(h -> {
      builder.append("HEADER: ")
             .append(h.getKey())
             .append(" = ")
             .append(h.getValue())
             .append(CRLF);
    });

    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
    queryStringDecoder.parameters().forEach((key, values) -> {
      builder.append("PARAM: ")
             .append(key)
             .append(" = ")
             .append(values)
             .append(CRLF);
    });

    appendDecoderResult(builder, request);
  }

  private void handleContent(ChannelHandlerContext ctx, HttpRequest request, HttpContent httpContent) {
    ByteBuf content = httpContent.content();

    if (content != null && content.isReadable()) {
      builder.append("CONTENT: ")
             .append(content.toString(CharsetUtil.UTF_8))
             .append(CRLF);
      appendDecoderResult(builder, request);
    }

    if (httpContent instanceof LastHttpContent) {
      builder.append("END OF CONTENT\r\n");

      LastHttpContent trailer = (LastHttpContent) httpContent;
      if (!trailer.trailingHeaders().isEmpty()) {
        builder.append(CRLF);

        trailer.trailingHeaders().names().forEach(name -> {
          trailer.trailingHeaders().getAll(name).forEach(value -> {
            builder.append("TRAINING HEADER: ")
                   .append(name)
                   .append(" = ")
                   .append(value)
                   .append(CRLF);
          });
        });
        builder.append(CRLF);
      }
      if (!writeResponse(ctx, request, trailer)) {
        // if keep-alive is off, close the connection once the content is fully written
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
      }
    }

  }

  private boolean writeResponse(ChannelHandlerContext ctx, HttpRequest req, HttpObject currentObj) {
    // Decide whether to close the connection or not.
    boolean keepAlive = HttpHeaders.isKeepAlive(req);

    // Build the response object.
    HttpResponseStatus status = currentObj.getDecoderResult().isSuccess() ? OK : BAD_REQUEST;
    ByteBuf content = Unpooled.copiedBuffer(builder.toString(), CharsetUtil.UTF_8);

    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, content);

    if (keepAlive) {
      // Add 'Content-Length' header only for a keep-alive connection.
      response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
      // Add keep alive header as per:
      // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
      response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
    }

    // Encode the cookie.
    String cookieString = req.headers().get(COOKIE);
    if (cookieString != null) {
      Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(cookieString);
      if (!cookies.isEmpty()) {
        for (Cookie cookie : cookies) {
          response.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode(cookie));
        }
      }
    } else {
      // Browser sent no cookie. Add some.
      response.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode("key1", "value1"));
      response.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode("key2", "value2"));
    }

    // Write the response
    ctx.write(response);

    return keepAlive;
  }

  private static void appendDecoderResult(StringBuilder builder, HttpObject o) {
    DecoderResult dr = o.getDecoderResult();
    if (dr.isSuccess()) {
      return;
    }
    builder.append(".. WITH DECODER FAILURE: ");
    builder.append(dr.cause());
    builder.append(CRLF);
  }

  private static void send100Continue(ChannelHandlerContext ctx) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
    ctx.write(response);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (cause != null) {
      log.error("예외 발생", cause);
    }
    ctx.close();
  }
}
