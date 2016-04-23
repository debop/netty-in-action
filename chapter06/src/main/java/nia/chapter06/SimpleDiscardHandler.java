package nia.chapter06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * SimpleDiscardHandler
 *
 * @author sunghyouk.bae@gmail.com
 */
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    // No need to do anything special
  }
}
