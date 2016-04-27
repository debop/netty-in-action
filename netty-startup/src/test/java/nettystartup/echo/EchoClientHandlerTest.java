package nettystartup.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import nia.utils.ByteBufEx;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EchoClientHandlerTest
 *
 * @author sunghyouk.bae@gmail.com
 */
public class EchoClientHandlerTest {

  @Test
  public void testEchoClientHandler() {
    EmbeddedChannel channel = new EmbeddedChannel(new EchoClientHandler());

    try {
      ByteBuf r = (ByteBuf) channel.readOutbound();
      assertThat(r).isNotNull();
      String str = ByteBufEx.toUtf8String(r);
      assertThat(str).isEqualTo("Netty rocks");
    } finally {
      channel.close();
    }
  }
}
