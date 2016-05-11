package nettybook.telnet.spring;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelnetServerInitializerV2 extends ChannelInitializer<SocketChannel> {

  private static final StringDecoder DECODER = new StringDecoder();
  private static final StringEncoder ENCODER = new StringEncoder();

  private static final TelnetServerHandlerV2 SERVER_HANDLER = new TelnetServerHandlerV2();

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline p = ch.pipeline();

    p.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    p.addLast(DECODER);
    p.addLast(ENCODER);
    p.addLast(SERVER_HANDLER);
  }
}
