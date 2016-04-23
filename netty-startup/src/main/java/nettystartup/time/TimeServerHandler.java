package nettystartup.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * TimeServerHandler
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ByteBuf time = ctx.alloc().buffer(8);

    time.writeLong(System.currentTimeMillis());
    log.debug("send time: {}", time.getLong(0));

    ChannelFuture f = ctx.writeAndFlush(time);
    f.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        assert f == future;
        ctx.close();
      }
    });
  }
}
