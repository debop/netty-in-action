package nettybook.ch6;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;

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
}
