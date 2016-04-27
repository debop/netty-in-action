package nettybook.buffer;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public abstract class AbstractByteBufTest {

  static final int CAPACITY = 4096;
  static final int BLOCK_SIZE = 128;

  private long seed;
  private Random random;
  private ByteBuf buffer;

  protected abstract ByteBuf newBuffer(int capacity);
  protected abstract ByteBuf[] components();

  protected boolean discardReadBytesDoesNotMoveWritableBytes() {
    return true;
  }

  @Before
  public void init() {
    buffer = newBuffer(CAPACITY);
    seed = System.currentTimeMillis();
    random = new Random(seed);
  }

  @After
  public void dispose() {
    if (buffer != null) {
      assertThat(buffer.release()).isTrue();
      assertThat(buffer.refCnt()).isEqualTo(0);

      try {
        buffer.release();
      } catch (Exception ignored) {}

      buffer = null;
    }
  }

  @Test
  public void initialState() {
    assertThat(buffer.capacity()).isEqualTo(CAPACITY);
    assertThat(buffer.readerIndex()).isEqualTo(0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void readerIndexBoundaryCheck1() {
    try {
      buffer.writerIndex(0);
    } catch (IndexOutOfBoundsException e) {
      Assertions.fail("");
    }
    buffer.readerIndex(-1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void readerIndexBoundaryCheck2() {
    try {
      buffer.writerIndex(buffer.capacity());
    } catch (IndexOutOfBoundsException e) {
      Assertions.fail("");
    }
    buffer.readerIndex(buffer.capacity() + 1);
  }
}
