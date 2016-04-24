package nettystartup.http;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
public class HttpStaticFileHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private String path;
  private String filename;

  public HttpStaticFileHandler(String path, String filename) {
    super(false);
    this.path = path;
    this.filename = filename;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    if (request.getUri().equals(path)) {
      sendStaticFile(ctx, request);
    } else {
      ctx.fireChannelRead(request);
    }
  }

  private void sendStaticFile(ChannelHandlerContext ctx, FullHttpRequest request) throws IOException {
    try {
      RandomAccessFile raf = new RandomAccessFile(filename, "r");
      long fileLength = raf.length();

      HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
      HttpHeaders.setContentLength(response, fileLength);
      response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=utf-8");
      if (HttpHeaders.isKeepAlive(request)) {
        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
      }
      ctx.write(response);
      ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength));
      ChannelFuture f = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
      if (!HttpHeaders.isKeepAlive(request)) {
        f.addListener(ChannelFutureListener.CLOSE);
      }
    } finally {
      request.release();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("error", cause);
  }
}
