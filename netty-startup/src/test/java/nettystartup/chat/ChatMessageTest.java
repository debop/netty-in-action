package nettystartup.chat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ChatMessageTest {

  @Test
  public void equal() {
    ChatMessage m1 = new ChatMessage("FROM", "me", "hello");
    ChatMessage m2 = new ChatMessage("FROM", "me", "hello");
    ChatMessage y1 = new ChatMessage("FROM", "you", "hello");
    ChatMessage y2 = new ChatMessage("FROM", "you", null);
    ChatMessage n1 = new ChatMessage("FROM", null, "hello");

    assertThat(m1).isEqualTo(m2);
    assertThat(m1).isNotEqualTo(y1);
    assertThat(y1).isNotEqualTo(y2);
    assertThat(y2).isNotEqualTo(n1);
  }

  @Test
  public void parsePing() {
    ChatMessage m = ChatMessage.parse("PING");
    assertThat(m.getCommand()).isEqualTo("PING");
    assertThat(m.getNickname()).isNull();
    assertThat(m.getText()).isNull();
  }

  @Test
  public void parseNickname() {
    ChatMessage m = ChatMessage.parse("JOIN:me");
    assertThat(m.getCommand()).isEqualTo("JOIN");
    assertThat(m.getNickname()).isEqualTo("me");
    assertThat(m.getText()).isNull();
  }

  @Test
  public void parseFrom() {
    ChatMessage expected = new ChatMessage("FROM", "you", "메시지 왔어요");
    assertThat(ChatMessage.parse("FROM:you 메시지 왔어요")).isEqualTo(expected);
  }

  @Test
  public void parseNoNick() {
    ChatMessage expected = new ChatMessage("SEND", null, "메시지:보내요");
    assertThat(ChatMessage.parse("SEND 메시지:보내요")).isEqualTo(expected);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parseWithNewLine() {
    ChatMessage.parse("이러면\n안돼요");
  }

  @Test
  public void testToString() {
    ChatMessage m = new ChatMessage("LEFT", "netty");
    assertThat(m.toString()).isEqualTo("LEFT:netty");
  }
}
