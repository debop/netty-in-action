package nia.chapter09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AbsIntegerEncoderTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class AbsIntegerEncoderTest {

  @Test
  public void testEncoded() {
    ByteBuf buf = Unpooled.buffer();
    for (int i = 1; i < 10; i++) {
      buf.writeInt(i * -1);
    }
    EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
    assertThat(channel.writeOutbound(buf)).isTrue();
    assertThat(channel.finish()).isTrue();

    for (int i = 1; i < 10; i++) {
      assertThat(channel.readOutbound()).isEqualTo(i);
    }
    assertThat(channel.readOutbound()).isNull();
  }
}
