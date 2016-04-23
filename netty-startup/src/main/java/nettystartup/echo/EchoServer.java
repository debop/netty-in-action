package nettystartup.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * EchoServer
 */
@Slf4j
public class EchoServer {

  public static void main(String[] args) throws Exception {
    EventLoopGroup baseGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(baseGroup, workerGroup)
       .channel(NioServerSocketChannel.class)
       .handler(new LoggingHandler(LogLevel.INFO))
       .childHandler(new ChannelInitializer<SocketChannel>() {
         @Override
         protected void initChannel(SocketChannel ch) throws Exception {
           ch.pipeline().addLast(new EchoServerHandler());
         }
       });
      ChannelFuture f = b.bind(8888).sync();
      log.debug("{} started and listening for connection on {}",
                EchoServer.class.getName(), f.channel().localAddress());
      f.channel().closeFuture().sync();

    } finally {
      workerGroup.shutdownGracefully();
      baseGroup.shutdownGracefully();
    }
  }
}
