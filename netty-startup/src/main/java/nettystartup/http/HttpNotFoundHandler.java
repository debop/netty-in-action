package nettystartup.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import nia.utils.ByteBufEx;

/**
 * HttpNotFoundHandler
 *
 * @author sunghyouk.bae@gmail.com
 */
public class HttpNotFoundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    ByteBuf buf = ByteBufEx.toByteBuf("Not Found");
    HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, buf);
    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=utf-8");
    if (HttpHeaders.isKeepAlive(request)) {
      response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
    }

    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
    ChannelFuture f = ctx.writeAndFlush(response);
    if (!HttpHeaders.isKeepAlive(request)) {
      f.addListener(ChannelFutureListener.CLOSE);
    }
//       .addListener((ChannelFuture f) -> {
//         if (!HttpHeaders.isKeepAlive(request)) {
//           f.channel().close();
//         }
//       });
  }
}
