package nettybook.ch5;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ChannelFuture channelFuture = ctx.writeAndFlush(msg);

    // channel 에 데이터 쓰기를 완료하면, CLOSE 를 수행하도록 합니다.
    channelFuture.addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.debug("예외발생", cause);
    ctx.close();
  }
}
