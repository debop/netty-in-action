package nia.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class EchoServerHandlerTest {

    @Test
    public void testEchoServer() {
        final String ping = "Netty rocks!";
        EmbeddedChannel channel = new EmbeddedChannel(new EchoServerHandler());
        try {
            final ByteBuf msg = ByteBufEx.toByteBuf(ping);
            channel.writeInbound(msg);
//            channel.writeAndFlush(msg);

            final ByteBuf result = (ByteBuf) channel.readOutbound();
            assertThat(result).isNotNull();

            final String response = ByteBufEx.toUtf8String(result);
            log.debug("Client received: {}", response);
            assertThat(response).isEqualTo(ping);
        } finally {
            channel.close();
        }
    }
}
