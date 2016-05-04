package nettybook.ch4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.compression.SnappyFramedDecoder;
import io.netty.handler.codec.compression.SnappyFramedEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EchoServerV3HandlerTest {

  @Test
  public void testEventPath() {

    String message = "Hello netty";
    ByteBuf msg = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);

    EmbeddedChannel channel = new EmbeddedChannel(new EchoServerV3FirstHandler(),
                                                  new EchoServerV3SecondHandler(),
                                                  new Base64Encoder(),
                                                  new Base64Decoder(),
                                                  new SnappyFramedEncoder(),
                                                  new SnappyFramedDecoder(),
                                                  new LoggingHandler(LogLevel.TRACE));


    channel.writeInbound(msg);

    ByteBuf received = (ByteBuf) channel.readOutbound();
    assertThat(received).isNotNull();
    final String receivedStr = received.toString(CharsetUtil.UTF_8);

    log.debug("received = {}", receivedStr);
    assertThat(receivedStr).isEqualTo(message);
  }

  @Test
  public void testEchoByHttpCodec() {
    String message = "Hello netty";
    ByteBuf msg = Unpooled.copiedBuffer(message, CharsetUtil.UTF_8);

    EmbeddedChannel channel = new EmbeddedChannel(new EchoServerV3FirstHandler(),
                                                  new EchoServerV3SecondHandler(),
                                                  new HttpServerCodec());


    channel.writeInbound(msg);

    ByteBuf received = (ByteBuf) channel.readOutbound();
    assertThat(received).isNotNull();
    final String receivedStr = received.toString(CharsetUtil.UTF_8);

    log.debug("received = {}", receivedStr);
    assertThat(receivedStr).isEqualTo(message);
  }

}
