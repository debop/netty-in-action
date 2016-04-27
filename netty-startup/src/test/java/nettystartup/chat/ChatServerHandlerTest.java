package nettystartup.chat;

import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ChatServerHandlerTest {

  private EmbeddedChannel ch;
  private ChatMessage m;
  private ChatMessage helo;
  private String nickname;

  @Before
  public void initChannel() {
    ch = new EmbeddedChannel(new ChatServerHandler());
    helo = null;
    while ((m = (ChatMessage) ch.readOutbound()) != null) {
      log.debug("read m : {}", m);
      // 중간에 HAVE 메시지가 있을 수도 있고, 마지막은 HELO
      helo = m;
    }
    nickname = helo.getNickname();
  }

  private ChatMessage writeAndRead(ChatMessage msg) {
    ch.writeInbound(msg);
    return (ChatMessage) ch.readOutbound();
  }

  @Test
  public void testHELO() {
    assertThat(helo.getCommand()).isEqualTo("HELO");
    assertThat(helo.getNickname()).isNotEmpty();
    assertThat(ch.attr(ChatServerHandler.nickAttr).get()).isEqualTo(helo.getNickname());
  }

  @Test
  public void testPING() {
    m = writeAndRead(new ChatMessage("PING"));
    assertThat(m).isEqualTo(new ChatMessage("PONG"));
  }

  @Test
  public void testQUIT() {
    m = writeAndRead(new ChatMessage("QUIT"));
    assertThat(m).isEqualTo(new ChatMessage("BYE", nickname));
    assertThat(ch.isOpen()).isFalse();
  }

  @Test
  public void testSEND() {
    m = writeAndRead(new ChatMessage("SEND", null, "보내는 메시지"));
    assertThat(m).isNotNull();
    assertThat(m.getCommand()).isEqualTo("FROM");
    assertThat(m.getNickname()).isEqualTo(nickname);
    assertThat(m.getText()).isEqualTo("보내는 메시지");
  }

  @Test
  public void testNICK() {
    m = writeAndRead(new ChatMessage("NICK", null, "New Nick"));
    assertThat(m).isEqualTo(new ChatMessage("NICK", nickname, "New_Nick"));
  }

  @Test
  public void testUnknwonCommand() {
    m = writeAndRead(new ChatMessage("UNKNOWN"));
    assertThat(m.getCommand()).isEqualTo("ERR");
  }
}
