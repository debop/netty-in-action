package nettystartup;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * NettyStartupUtil
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class NettyStartupUtil {

  private NettyStartupUtil() {}

  public static void runServer(int port, ChannelHandler childHandler) throws Exception {
    runServer(port, childHandler, b -> {
    });
  }

  public static void runServer(int port,
                               ChannelHandler childHandler,
                               Consumer<ServerBootstrap> block) throws Exception {

    EventLoopGroup baseGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(baseGroup, workerGroup)
               .channel(NioServerSocketChannel.class)
               .handler(new LoggingHandler(LogLevel.INFO))
               .childHandler(childHandler);

      block.accept(bootstrap);

      Channel ch = bootstrap.bind(port).sync().channel();
      log.info("Ready for 0.0.0.0:{}", port);
      ch.closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      baseGroup.shutdownGracefully();
    }
  }
}
