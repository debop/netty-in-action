package nettybook.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ByteBuf 가 ByteBuffer 보다 성능이 좋다.
 *
 * 1. 별도의 읽기, 쓰기 인덱스
 * 2. flip 메소드 없이 읽기/쓰기 가능
 * 3. 가변 바이트 버퍼
 * 4. 바이트 버퍼 풀
 * 5. 복합 버퍼
 * 6. ByteBuffer 와 ByteBuf 상호 변환
 */
public class ByteBufTest {

  private final String sourceData = "hello world";

  @Test
  public void createUnpooledHeapByteBuf() {
    ByteBuf buf = Unpooled.buffer(11);
    testBuffer(buf, false);
  }

  @Test
  public void createUnpooledDirectByteBuf() {
    ByteBuf buf = Unpooled.directBuffer(11);
    testBuffer(buf, true);
  }

  @Test
  public void createPooledHeapByteBuf() {
    ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
    testBuffer(buf, false);
  }

  @Test
  public void createPooledDirectByteBuf() {
    ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
    testBuffer(buf, true);
  }

  private void testBuffer(ByteBuf buf, boolean isDirect) {
    assertThat(buf.capacity()).isEqualTo(11);
    assertThat(buf.isDirect()).isEqualTo(isDirect);
    assertThat(buf.readableBytes()).isEqualTo(0);
    assertThat(buf.writableBytes()).isEqualTo(11);

    buf.writeInt(65537);
    assertThat(buf.readableBytes()).isEqualTo(4);
    assertThat(buf.writableBytes()).isEqualTo(7);  // 11-4 = 7

    assertThat(buf.readShort()).isEqualTo((short) 1);  // 65537 -> 0x10001 -> 1
    assertThat(buf.readableBytes()).isEqualTo(2); // short = 2 byte 를 읽었으므로
    assertThat(buf.writableBytes()).isEqualTo(7);

    assertThat(buf.isReadable()).isTrue();

    buf.clear();

    assertThat(buf.readableBytes()).isEqualTo(0);
    assertThat(buf.writableBytes()).isEqualTo(11);

    // 가변이 가능하다 !!!!
    assertThat(buf.capacity()).isEqualTo(11);
    assertThat(buf.isDirect()).isEqualTo(isDirect);


    buf.writeBytes(sourceData.getBytes(CharsetUtil.UTF_8));
    assertThat(buf.readableBytes()).isEqualTo(11);
    assertThat(buf.writableBytes()).isEqualTo(0);

    assertThat(buf.toString(CharsetUtil.UTF_8)).isEqualTo(sourceData);

    buf.capacity(6);
    assertThat(buf.toString(CharsetUtil.UTF_8)).isEqualTo("hello ");
    assertThat(buf.capacity()).isEqualTo(6);

    buf.capacity(13);
    assertThat(buf.toString(CharsetUtil.UTF_8)).isEqualTo("hello ");
    assertThat(buf.capacity()).isEqualTo(13);

    buf.writeBytes("world".getBytes(CharsetUtil.UTF_8));
    assertThat(buf.toString(CharsetUtil.UTF_8)).isEqualTo(sourceData);

    assertThat(buf.capacity()).isEqualTo(13);
    assertThat(buf.writableBytes()).isEqualTo(2);
  }

  @Test
  public void writeUnsignedNumbers() {
    ByteBuf buf = Unpooled.buffer(11);
    buf.writeShort(-1);

    assertThat(buf.getUnsignedShort(0)).isEqualTo(0xFFFF);
  }

  @Test
  public void orderedByteBuf() {
    ByteBuf buf = Unpooled.buffer();
    assertThat(buf.order()).isEqualTo(ByteOrder.BIG_ENDIAN);

    buf.writeShort(1);
    buf.markReaderIndex();
    assertThat(buf.readShort()).isEqualTo((short) 1);

    buf.resetReaderIndex();

    ByteBuf littleEndianBuf = buf.order(ByteOrder.LITTLE_ENDIAN);
    assertThat(littleEndianBuf.readShort()).isEqualTo((short) 0x0100);  // 0x0001 -> 0x0100
  }

  @Test
  public void convertNettyBufferToJavaBuffer() {
    ByteBuf buf = Unpooled.buffer(11);
    buf.writeBytes(sourceData.getBytes(CharsetUtil.UTF_8));

    assertThat(buf.toString(CharsetUtil.UTF_8)).isEqualTo(sourceData);

    ByteBuffer nioByteBuffer = buf.nioBuffer();
    assertThat(nioByteBuffer).isNotNull();

    String str = new String(nioByteBuffer.array(), nioByteBuffer.arrayOffset(), nioByteBuffer.remaining());
    assertThat(str).isNotEmpty().isEqualTo(sourceData);
  }

  @Test
  public void convertJavaBufferToNettyBuffer() {
    ByteBuffer byteBuffer = ByteBuffer.wrap(sourceData.getBytes(CharsetUtil.UTF_8));
    ByteBuf nettyBuf = Unpooled.wrappedBuffer(byteBuffer);

    assertThat(nettyBuf.toString(CharsetUtil.UTF_8)).isEqualTo(sourceData);
  }


}
