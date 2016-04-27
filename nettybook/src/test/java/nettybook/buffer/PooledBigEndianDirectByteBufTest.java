package nettybook.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;


public class PooledBigEndianDirectByteBufTest extends AbstractByteBufTest {

  private ByteBuf buffer;

  @Override
  protected ByteBuf newBuffer(int capacity) {
    buffer = PooledByteBufAllocator.DEFAULT.directBuffer(capacity);
    assertThat(buffer.order()).isEqualTo(ByteOrder.BIG_ENDIAN);
    assertThat(buffer.writerIndex()).isEqualTo(0);
    return buffer;
  }

  @Override
  protected ByteBuf[] components() {
    return new ByteBuf[] { buffer };
  }
}
