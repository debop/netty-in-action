package nettystartup.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;

@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    final ByteBuf in = (ByteBuf) msg;
    if (in != null) {
      final String str = ByteBufEx.toUtf8String(in);
      log.debug("Received: {}", str);
      ctx.write(in);
    }
  }
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("echo server error", cause);
    ctx.close();
  }
}
