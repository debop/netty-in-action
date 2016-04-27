package nettystartup.http;

import io.netty.channel.FileRegion;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * HttpStaticFileHandlerTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class HttpStaticFileHandlerTest {

  @Test
  public void testHttpServerHandler() throws Exception {
    String index = HttpStaticServer.index;
    EmbeddedChannel channel = new EmbeddedChannel(new HttpStaticFileHandler("/", index));

    try {
      channel.writeInbound(new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/"));
      assertThat(channel.readOutbound()).isInstanceOf(HttpResponse.class);
      FileRegion content = (FileRegion) channel.readOutbound();
      assertThat(content.count()).isGreaterThan(0);
    } finally {
      channel.close();
    }
  }
}
