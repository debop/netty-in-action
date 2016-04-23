package nettystartup.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TimeServerHandlerTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class TimeServerHandlerTest {

  @Test
  public void testTimeServerHandler() {
    EmbeddedChannel channel = new EmbeddedChannel(new TimeServerHandler());

    ByteBuf r = (ByteBuf) channel.readOutbound();
    assertThat(r).isNotNull();

    long time = r.readLong();
    log.debug("received time= {}", time);
    assertThat(time).isGreaterThan(0L)
                    .isLessThanOrEqualTo(System.currentTimeMillis());
  }
}
