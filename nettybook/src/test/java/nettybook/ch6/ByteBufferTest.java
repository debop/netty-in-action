package nettybook.ch6;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ByteBufferTest {

  @Test
  public void allocateByteBuffer() {
    byte[] source = "Hello world!".getBytes();

    ByteBuffer firstBuffer = ByteBuffer.allocate(source.length);
    log.debug("초기 상태={}", firstBuffer);

    for (byte item : source) {
      firstBuffer.put(item);
      log.debug("현재 상태={}", firstBuffer);
    }
  }

  @Test
  public void byteBufferPosition() {
    ByteBuffer buffer = ByteBuffer.allocate(11);
    log.debug("initial status = {}", buffer);

    buffer.put((byte) 1);
    buffer.put((byte) 2);
    assertThat(buffer.position()).isEqualTo(2);

    buffer.rewind();
    assertThat(buffer.position()).isEqualTo(0);

    assertThat(buffer.get()).isEqualTo((byte) 1);
    assertThat(buffer.position()).isEqualTo(1);

    log.debug("read status = {}", buffer);
  }

  @Test
  public void byteBufferWrite() {
    ByteBuffer buffer = ByteBuffer.allocate(11);
    log.debug("initial status = {}", buffer);

    buffer.put((byte) 1);
    buffer.put((byte) 2);
    buffer.put((byte) 3);
    buffer.put((byte) 4);

    assertThat(buffer.position()).isEqualTo(4);
    assertThat(buffer.limit()).isEqualTo(11);
    assertThat(buffer.capacity()).isEqualTo(11);

    buffer.flip();
    assertThat(buffer.position()).isEqualTo(0);
    assertThat(buffer.limit()).isEqualTo(4);
    assertThat(buffer.capacity()).isEqualTo(11);

    log.debug("read status = {}", buffer);
  }

  @Test
  public void byteBufferRead() {
    byte[] tempArray = { 1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0 };
    ByteBuffer firstBuffer = ByteBuffer.wrap(tempArray);
    assertThat(firstBuffer.position()).isEqualTo(0);
    assertThat(firstBuffer.limit()).isEqualTo(tempArray.length);
    assertThat(firstBuffer.capacity()).isEqualTo(tempArray.length);

    assertThat(firstBuffer.get()).isEqualTo((byte) 1);
    assertThat(firstBuffer.get()).isEqualTo((byte) 2);
    assertThat(firstBuffer.get()).isEqualTo((byte) 3);
    assertThat(firstBuffer.get()).isEqualTo((byte) 4);
    assertThat(firstBuffer.position()).isEqualTo(4);
    assertThat(firstBuffer.capacity()).isEqualTo(tempArray.length);

    firstBuffer.flip();

    assertThat(firstBuffer.position()).isEqualTo(0);
    assertThat(firstBuffer.limit()).isEqualTo(4);
    assertThat(firstBuffer.capacity()).isEqualTo(11);

    log.debug("get(3)={}", firstBuffer.get(3));

    assertThat(firstBuffer.position()).isEqualTo(0);
    assertThat(firstBuffer.limit()).isEqualTo(4);
  }
}
