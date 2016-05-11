package nettybook.telnet.tests;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static io.netty.util.CharsetUtil.UTF_8;

@Slf4j
public class StripDecoderTest {

  @Test
  public void testStripDecoder() {
    String writeData = "test";

    EmbeddedChannel channel = new EmbeddedChannel(new StripDecoder());

//    ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes(UTF_8));
//    channel.writeInbound(request);
    channel.writeInbound(writeData);

    ByteBuf response = (ByteBuf) channel.readOutbound();

    Assertions.assertThat(response.toString(UTF_8)).isEqualTo(writeData + "a");

    channel.finish();
  }
}
