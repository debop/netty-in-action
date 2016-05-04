package nettybook.ch5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServerWithFuture {

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
           ch.pipeline().addLast(new EchoServerHandlerWithFuture());
         }
       });

      ChannelFuture bindFuture = b.bind(8888);
      log.debug("Bind 시작 ...");
      bindFuture.sync();
      log.debug("Bind 완료.");

      Channel serverChannel = bindFuture.channel();
      ChannelFuture closeFuture = serverChannel.closeFuture();
      closeFuture.sync();

    } finally {
      workGroup.shutdownGracefully();
      baseGroup.shutdownGracefully();
    }
  }
}
