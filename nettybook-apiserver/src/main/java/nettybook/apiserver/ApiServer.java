package nettybook.apiserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import nettybook.apiserver.config.ApiServerSetting;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetSocketAddress;

@Component
public class ApiServer {

  @Named("tcpSocketAddress") private InetSocketAddress address;
  @Inject ApiServerSetting apiSetting;

  @SneakyThrows({ InterruptedException.class })
  public void start() {
    EventLoopGroup baseGroup = new NioEventLoopGroup(apiSetting.getBaseThreadCount());
    EventLoopGroup workGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(baseGroup, workGroup)
       .channel(NioServerSocketChannel.class)
       .handler(new LoggingHandler(LogLevel.INFO))
       .childHandler(new ApiServerInitializer(null));

      Channel ch = b.bind(8081).sync().channel();

      ChannelFuture future = ch.closeFuture();
      future.sync();

    } finally {
      baseGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }
}
