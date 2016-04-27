package nettystartup.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DiscardServerHandlerTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class DiscardServerHandlerTest {

  @Test
  public void testDiscardServerHandler() {
    String m = "discard test";
    EmbeddedChannel ch = new EmbeddedChannel(new DiscardServerHandler());

    try {
      ByteBuf in = ByteBufEx.toByteBuf(m);
      ch.writeInbound(in);

      ByteBuf r = (ByteBuf) ch.readOutbound();
      assertThat(r).isNull();
    } finally {
      ch.close();
    }
  }
}
