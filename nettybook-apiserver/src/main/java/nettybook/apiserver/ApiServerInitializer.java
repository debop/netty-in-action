package nettybook.apiserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;


public class ApiServerInitializer extends ChannelInitializer<SocketChannel> {

  private final SslContext sslCtx;

  public ApiServerInitializer(SslContext sslCtx) {
    this.sslCtx = sslCtx;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {

  }
}
