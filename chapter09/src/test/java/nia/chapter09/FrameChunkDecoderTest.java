package nia.chapter09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * FrameChunkDecoderTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        assertThat(channel.writeInbound(input.readBytes(2))).isTrue();
        try {
            channel.writeInbound(input.readBytes(4));
            fail();
        } catch (TooLongFrameException e) {
            // expected exception
        }
        assertThat(channel.writeInbound(input.readBytes(3))).isTrue();
        assertThat(channel.finish()).isTrue();

        ByteBuf read = (ByteBuf) channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(2));
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertThat(read).isEqualTo(buf.skipBytes(4).readSlice(3));
        read.release();
        buf.release();
    }
}
