package nettybook.ch4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
public class EchoServerV2HandlerTest {

  @Test
  public void testEchoHandler() {
    final String sendMessage = "Hello netty";

    EmbeddedChannel channel = new EmbeddedChannel(new EchoServerV2Handler());

    ByteBuf msg = Unpooled.copiedBuffer(sendMessage, CharsetUtil.UTF_8);
    assertThat(channel.writeInbound(msg)).isFalse();

    ByteBuf received = (ByteBuf) channel.readOutbound();

    log.debug("received = {}", received.toString(CharsetUtil.UTF_8));
    assertThat(received.toString(CharsetUtil.UTF_8)).isEqualTo(sendMessage);

  }
}
