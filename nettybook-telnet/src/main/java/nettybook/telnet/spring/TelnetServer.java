package nettybook.telnet.spring;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nettybook.telnet.TelnetServerInitializer;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetSocketAddress;

@Component
@Slf4j
public class TelnetServer {

  @Inject
  @Named("tcpSocketAddress")
  private InetSocketAddress address;

  @SneakyThrows({ InterruptedException.class })
  public void start() {
    EventLoopGroup baseGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(baseGroup, workGroup)
       .channel(NioServerSocketChannel.class)
       .handler(new LoggingHandler(LogLevel.INFO))
       .childHandler(new TelnetServerInitializer());

      ChannelFuture future = b.bind(address).sync();
      future.channel().closeFuture().sync();
    } finally {
      baseGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }
}
