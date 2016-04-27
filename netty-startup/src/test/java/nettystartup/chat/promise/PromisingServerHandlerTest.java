package nettystartup.chat.promise;

import io.netty.channel.embedded.EmbeddedChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PromisingServerHandlerTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class PromisingServerHandlerTest {

  @Test
  @SneakyThrows
  public void testPromising() {
    EmbeddedChannel channel = new EmbeddedChannel(new PromisingServerHandler());

    try {
      channel.writeInbound("send message");

      String res = (String) channel.readOutbound();
      assertThat(res).isNotEmpty();
      log.debug("res: {}", res);

      Thread.sleep(1500);
    } finally {
      channel.close();
    }
  }
}
