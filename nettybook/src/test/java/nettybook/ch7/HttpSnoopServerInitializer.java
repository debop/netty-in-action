package nettybook.ch7;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import lombok.Getter;

@Getter
public class HttpSnoopServerInitializer extends ChannelInitializer<SocketChannel> {

  private final SslContext sslContext;

  public HttpSnoopServerInitializer(SslContext sslContext) {
    this.sslContext = sslContext;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline p = ch.pipeline();

    if (sslContext != null) {
      p.addLast(sslContext.newHandler(ch.alloc()));
    }

    p.addLast(new HttpRequestDecoder());
    p.addLast(new HttpObjectAggregator(1024 * 1024));
    p.addLast(new HttpResponseEncoder());
    p.addLast(new HttpSnoopServerHandler());
  }
}
