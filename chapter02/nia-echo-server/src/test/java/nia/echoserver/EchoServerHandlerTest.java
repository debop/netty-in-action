package nia.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;
import org.junit.Test;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class EchoServerHandlerTest {

    @Test
    public void testEchoServer() {
        EmbeddedChannel channel = new EmbeddedChannel(new EchoServerHandler());
        try {
            final ByteBuf msg = ByteBufEx.toByteBuf("PING");
            channel.writeInbound(msg);

            final ByteBuf result = (ByteBuf) channel.readOutbound();
            log.debug("Client received: {}", ByteBufEx.toUtf8String(result));
        } finally {
            channel.close();
        }
    }
}
