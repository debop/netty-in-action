package nettystartup.chat;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class ChatMessageCodecTest {

  ChatMessageCodec codec = new ChatMessageCodec();

  @Test
  public void testEncode() throws Exception {
    List<Object> out = FastList.newList();

    codec.encode(null, new ChatMessage("JOIN", "netty"), out);
    codec.encode(null, new ChatMessage("FROM", "netty", "안녕하세요. 방가방가"), out);

    assertThat(out).hasSize(2);
    assertThat(out.get(0).toString()).isEqualTo("JOIN:netty\n");
    assertThat(out.get(1).toString()).isEqualTo("FROM:netty 안녕하세요. 방가방가\n");
  }

  @Test
  public void testDecode() throws Exception {
    List<Object> out = FastList.newList();

    codec.decode(null, "JOIN:netty", out);
    codec.decode(null, "FROM:netty 안녕하세요. 방가방가", out);

    assertThat(out).hasSize(2);
    assertThat(out.get(0)).isEqualTo(new ChatMessage("JOIN", "netty"));
    assertThat(out.get(1)).isEqualTo(new ChatMessage("FROM", "netty", "안녕하세요. 방가방가"));
  }
}
