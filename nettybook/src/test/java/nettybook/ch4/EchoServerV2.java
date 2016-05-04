package nettybook.ch4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class EchoServerV2 {

  public static void main(String[] args) throws Exception {
    EventLoopGroup baseGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(baseGroup, workGroup)
       .channel(NioServerSocketChannel.class)
       .childHandler(new ChannelInitializer<SocketChannel>() {
         @Override
         protected void initChannel(SocketChannel ch) throws Exception {
           ch.pipeline().addLast(new EchoServerV2Handler());
         }
       });

      ChannelFuture f = b.bind(8888).sync();
      f.channel().closeFuture().sync();
    } finally {
      workGroup.shutdownGracefully();
      baseGroup.shutdownGracefully();
    }
  }
}
