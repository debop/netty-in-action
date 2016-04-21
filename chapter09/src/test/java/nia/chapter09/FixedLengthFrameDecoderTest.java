package nia.chapter09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FixedLengthFrameDecoderTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        assertThat(channel.writeInbound(input.retain())).isTrue();
        assertThat(channel.finish()).isTrue();

        ByteBuf read = (ByteBuf) channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        assertThat(channel.readInbound()).isNull();
        buf.release();
    }

}
