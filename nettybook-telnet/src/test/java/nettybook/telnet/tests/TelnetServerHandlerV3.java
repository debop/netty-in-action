package nettybook.telnet.tests;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelnetServerHandlerV3 extends SimpleChannelInboundHandler<String> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.write(ResponseGenerator.makeHello());
    ctx.flush();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    ResponseGenerator generator = new ResponseGenerator(msg);

    String response = generator.response();

    ChannelFuture future = ctx.write(response);

    if (generator.isClose()) {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("예외 발생", cause);
    ctx.close();
  }
}
