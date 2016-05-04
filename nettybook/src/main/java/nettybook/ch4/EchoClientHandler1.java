package nettybook.ch4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

@Slf4j
public class EchoClientHandler1 extends ChannelOutboundHandlerAdapter {

  @Override
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
    log.debug("connect 완료. server addr={}, client addr={}", remoteAddress, localAddress);
    super.connect(ctx, remoteAddress, localAddress, promise);
  }
  @Override
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    log.debug("close");
    super.close(ctx, promise);
  }
}
