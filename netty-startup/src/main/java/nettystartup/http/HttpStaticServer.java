package nettystartup.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;
import nettystartup.NettyStartupUtil;

/**
 * HttpStaticServer
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class HttpStaticServer {

  static String index = System.getProperty("user.dir") + "/res/http/index.html";

  public static void main(String[] args) throws Exception {

    log.debug("index :{}", index);

    NettyStartupUtil.runServer(8020, new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new HttpStaticFileHandler("/", index));
        p.addLast(new HttpNotFoundHandler());
      }
    });
  }
}
