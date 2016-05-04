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

  @Override
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    log.debug("deregistered");
    super.deregister(ctx, promise);
  }
  @Override
  public void read(ChannelHandlerContext ctx) throws Exception {
    log.debug("read");
    super.read(ctx);
  }
  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    log.debug("write... msg={}", msg);
    super.write(ctx, msg, promise);
  }
  @Override
  public void flush(ChannelHandlerContext ctx) throws Exception {
    log.debug("flush");
    super.flush(ctx);
  }
  @Override
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    log.debug("disconnect...");
    super.disconnect(ctx, promise);
  }
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("에러 발생", cause);
    super.exceptionCaught(ctx, cause);
  }
}
