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
}
