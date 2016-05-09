package nettybook.ch5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServerHandlerWithFuture extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    final int writeMessageSize = ((ByteBuf) msg).readableBytes();
    ChannelFuture channelFuture = ctx.writeAndFlush(msg);

    channelFuture.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        log.debug("전송한 byte : {}", writeMessageSize);
        future.channel().close();
      }
    });
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.debug("예외발생", cause);
    ctx.close();
  }
}
