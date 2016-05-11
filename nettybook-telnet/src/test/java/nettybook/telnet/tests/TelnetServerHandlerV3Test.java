package nettybook.telnet.tests;

import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TelnetServerHandlerV3Test {

  @Test
  public void testConnect() {

    ResponseGenerator.makeHello();
    EmbeddedChannel channel = new EmbeddedChannel(new TelnetServerHandlerV3());

    String expected = (String) channel.readOutbound();
    assertThat(expected).isNotNull();

    assertThat(expected).startsWith("환영합니다");

    String request = "hello";
    expected = "입력하신 명령이 '" + request + "' 입니까?\r\n";

    channel.writeInbound(request);

    String response = (String) channel.readOutbound();

    assertThat(response).isEqualTo(expected);

    channel.finishAndReleaseAll();
  }
}
