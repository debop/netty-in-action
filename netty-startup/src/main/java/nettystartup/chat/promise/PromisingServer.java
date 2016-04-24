package nettystartup.chat.promise;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import nettystartup.NettyStartupUtil;

/**
 * PromisingServer
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class PromisingServer {

  public static void main(String[] args) throws Exception {
    NettyStartupUtil.runServer(8031, new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
          .addLast(new LineBasedFrameDecoder(1024))
          .addLast(new StringDecoder(), new StringEncoder())
          .addLast(new PromisingServerHandler());
      }
    });
  }
}
