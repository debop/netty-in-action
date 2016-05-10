package nettybook.ch7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.File;

@Slf4j
public class HttpSnoopServer {

  private static final int PORT = 8443;

  public static void main(String[] args) throws Exception {
    SslContext sslContext = buildSslContext();

    EventLoopGroup baseGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();

      b.group(baseGroup, workGroup)
       .channel(NioServerSocketChannel.class)
       .handler(new LoggingHandler(LogLevel.INFO))
       .childHandler(new HttpSnoopServerInitializer(sslContext));

      Channel ch = b.bind(PORT).sync().channel();
      ch.closeFuture().sync();
    } finally {
      baseGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }

  private static SslContext buildSslContext() {
    try {
      File certChainFile = new File("netty.crt");
      File keyFile = new File("privatekey.pem");

      return SslContextBuilder.forServer(certChainFile, keyFile, "@real21").build();
    } catch (SSLException e) {
      log.error("SslContext 생성에 실패했습니다.", e);
      return null;
    }
  }
}
