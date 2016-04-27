package nettystartup.websocket;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import nettystartup.chat.ChatServerHandler;
import org.junit.Test;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class WebChatHandlerTest {

  @Test
  public void pipelineUpdate() throws Exception {
    EmbeddedChannel ch = new EmbeddedChannel(new WebChatHandler());
    ChannelPipeline p = ch.pipeline();

    assertThat(p.first()).isInstanceOf(WebChatHandler.class);
    assertThat(p.get(WebSocketChatCodec.class)).isNotNull();
    assertThat(p.get(ChatServerHandler.class)).isNotNull();

    Object[] klasses = StreamSupport.stream(p.spliterator(), false)
                                    .limit(3)
                                    .map(e -> e.getValue().getClass())
                                    .toArray();
    Object[] expected = new Object[] {
        WebChatHandler.class,
        WebSocketChatCodec.class,
        ChatServerHandler.class
    };

    assertThat(klasses).isEqualTo(expected);

    Object res = ch.readOutbound();
    TextWebSocketFrame twsFrame = (TextWebSocketFrame) res;
    log.debug("read: {}", twsFrame.text());
  }
}
