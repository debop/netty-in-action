package nettybook.ch5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import lombok.NonNull;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FutureHandlerTest {

  static final String sendStr = "Hello netty";
  static final ByteBuf sendMsg = Unpooled.copiedBuffer(sendStr, CharsetUtil.UTF_8);

  @Test
  public void testEchoServerHandler() {
    testHandler(new EchoServerHandler());
  }

  @Test
  public void testEchoServerHandlerWithFuture() {
    testHandler(new EchoServerHandlerWithFuture());
  }

  private void testHandler(@NonNull ChannelInboundHandlerAdapter handler) {
    EmbeddedChannel channel = new EmbeddedChannel(handler);

    channel.writeInbound(sendMsg);

    final ByteBuf receivedMsg = (ByteBuf) channel.readOutbound();

    assertThat(receivedMsg).isNotNull();

    final String receivedStr = receivedMsg.toString(CharsetUtil.UTF_8);
    assertThat(receivedStr).isEqualTo(sendStr);
  }
}
