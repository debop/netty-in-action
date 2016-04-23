package nettystartup.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EchoServerHandlerTest {

  @Test
  public void testEcho() {
    String m = "echo test\n";
    EmbeddedChannel channel = new EmbeddedChannel(new EchoServerHandler());
    ByteBuf in = ByteBufEx.toByteBuf(m);
    channel.writeInbound(in);

    ByteBuf r = (ByteBuf) channel.readOutbound();
    ReferenceCountUtil.releaseLater(r);

    assertThat(r).isNotNull();
    final String str = ByteBufEx.toUtf8String(r);
    log.debug("echo message: {}", str);
    assertThat(str).isEqualTo(m);
  }
}
