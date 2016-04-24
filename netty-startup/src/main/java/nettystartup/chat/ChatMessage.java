package nettystartup.chat;

import kesti4j.core.AbstractValueObject;
import kesti4j.core.ToStringHelper;
import kesti4j.core.utils.HashEx;
import lombok.Getter;
import lombok.Setter;

/**
 * ChatMessage
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class ChatMessage extends AbstractValueObject {

  public static ChatMessage parse(String line) {
    if (line.contains("\n"))
      throw new IllegalArgumentException();

    String[] tokens = line.split("\\s", 2);
    String command = tokens[0];
    ChatMessage m = null;
    if (command.contains(":")) {
      String[] t = command.split(":", 2);
      m = new ChatMessage(t[0], t[1]);
    } else {
      m = new ChatMessage(command);
    }
    if (tokens.length > 1)
      m.text = tokens[1];

    return m;
  }

  private final String command;
  private final String nickname;
  private String text;

  public ChatMessage(String command) {
    this(command, null, null);
  }
  public ChatMessage(String command, String nickname) {
    this(command, nickname, null);
  }
  public ChatMessage(String command, String nickname, String text) {
    this.command = command;
    this.nickname = nickname;
    this.text = text;
  }

  private boolean bothNullOrEqual(String s1, String s2) {
    return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
  }

  @Override
  public int hashCode() {
    return HashEx.compute(command, nickname, text);
  }
  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("command", command)
                .add("nickname", nickname)
                .add("text", text);
  }


}
