package nettystartup.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * EchoClient
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@Getter
@Setter
public class EchoClient {
  private final String host;
  private final int port;

  public EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
       .channel(NioSocketChannel.class)
       .remoteAddress(new InetSocketAddress(host, port))
       .handler(new LoggingHandler(LogLevel.INFO))
       .handler(new ChannelInitializer<SocketChannel>() {
         @Override
         protected void initChannel(SocketChannel ch) throws Exception {
           ch.pipeline().addLast(new EchoClientHandler());
         }
       });
      ChannelFuture f = b.connect().sync();
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
//    if (args.length != 2) {
//      System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
//      return;
//    }
//
//    final String host = args[0];
//    final int port = Integer.parseInt(args[1]);
    String host = "localhost";
    int port = 8888;
    new EchoClient(host, port).start();
  }
}
