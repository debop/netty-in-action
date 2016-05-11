package nettybook.telnet.tests;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.util.CharsetUtil;
import kesti4j.core.utils.StringEx;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CodecTest {

  @Test
  public void testBase64Encoder() {
    String originStr = "안녕하세요. 홈페이지는 http://www.kesti.co.kr 입니다.";
    ByteBuf request = Unpooled.wrappedBuffer(originStr.getBytes(CharsetUtil.UTF_8));

    Base64Encoder encoder = new Base64Encoder();
    EmbeddedChannel channel = new EmbeddedChannel(encoder);

    channel.writeOutbound(request);
    ByteBuf response = (ByteBuf) channel.readOutbound();

    String expected = StringEx.byteArrayToBase64String(StringEx.toUtf8Bytes(originStr));
    assertThat(response.toString(CharsetUtil.UTF_8)).isEqualToIgnoringWhitespace(expected);
  }

  @Test
  public void testDelimiterBasedFrameDecoder() {
    String writeData = "안녕하세요\r\nHello World\r\n";
    String firstResponse = "안녕하세요\r\n";
    String secondResponse = "Hello World\r\n";

    DelimiterBasedFrameDecoder decoder = new DelimiterBasedFrameDecoder(8192,
                                                                        false,
                                                                        Delimiters.lineDelimiter());

    EmbeddedChannel channel = new EmbeddedChannel(decoder);

    ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes(CharsetUtil.UTF_8));
    boolean result = channel.writeInbound(request);
    assertThat(result).isTrue();

    ByteBuf response = null;

    response = (ByteBuf) channel.readInbound();
    assertThat(response.toString(CharsetUtil.UTF_8)).isEqualTo(firstResponse);

    response = (ByteBuf) channel.readInbound();
    assertThat(response.toString(CharsetUtil.UTF_8)).isEqualTo(secondResponse);
  }
}
