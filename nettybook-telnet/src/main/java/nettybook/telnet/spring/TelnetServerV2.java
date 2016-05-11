package nettybook.telnet.spring;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetSocketAddress;

public class TelnetServerV2 {

  @Inject
  @Named("tcpSocketAddress")
  private InetSocketAddress address;

  @Inject TelnetServerSetting telnetSetting;

  @SneakyThrows({ InterruptedException.class })
  public void start() {
    EventLoopGroup baseGroup = new NioEventLoopGroup(telnetSetting.getBaseThreadCount());
    EventLoopGroup workGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(baseGroup, workGroup)
       .channel(NioServerSocketChannel.class)
       .handler(new LoggingHandler(LogLevel.INFO))
       .childHandler(new TelnetServerInitializerV2());

      ChannelFuture future = b.bind(address).sync();
      future.channel().closeFuture().sync();
    } finally {
      baseGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }
}
