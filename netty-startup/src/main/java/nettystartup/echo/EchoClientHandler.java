package nettystartup.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;

@Slf4j
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    final String msg = "Netty rocks";
    ctx.writeAndFlush(ByteBufEx.toByteBuf(msg));
    log.info("Client send: {}", msg);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    String s = ByteBufEx.toUtf8String(msg);
    log.info("Client received: {}", s);
  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("client error", cause);
    ctx.close();
  }
}
