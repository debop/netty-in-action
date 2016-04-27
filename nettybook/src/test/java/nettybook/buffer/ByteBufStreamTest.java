package nettybook.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.EOFException;

import static io.netty.util.internal.EmptyArrays.EMPTY_BYTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.fail;

@Slf4j
public class ByteBufStreamTest {

  @Test
  public void createByteBufOutputStream() {
    assertThatThrownBy(() -> new ByteBufOutputStream(null))
        .isInstanceOf(NullPointerException.class);
  }
  @Test
  public void createByteBufInputStream() {
    ByteBuf buf = Unpooled.buffer(0, 65536);

    assertThatThrownBy(() -> new ByteBufInputStream(null))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(() -> new ByteBufInputStream(null, 0))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(() -> new ByteBufInputStream(buf, -1))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> new ByteBufInputStream(buf, buf.capacity() + 1))
        .isInstanceOf(IndexOutOfBoundsException.class);
  }

  @Test
  public void testAll() throws Exception {
    ByteBuf buf = Unpooled.buffer(0, 65536);

    ByteBufOutputStream out = new ByteBufOutputStream(buf);
    assertThat(out.buffer()).isEqualTo(buf);
    out.writeBoolean(true);
    out.writeBoolean(false);
    out.writeByte(42);
    out.writeByte(224);
    out.writeBytes("Hello, World!");
    out.writeChars("Hello, World");
    out.writeChar('!');
    out.writeDouble(42.0);
    out.writeFloat(42.0F);
    out.writeInt(42);
    out.writeLong(42);
    out.writeShort(42);
    out.writeShort(49152);
    out.writeUTF("Hello, World!");
    out.writeBytes("The first line\r\r\n");
    out.write(EMPTY_BYTES);
    out.write(new byte[] { 1, 2, 3, 4 });
    out.write(new byte[] { 1, 3, 3, 4 }, 0, 0);
    out.close();

    ByteBufInputStream in = new ByteBufInputStream(buf);
    assertThat(in.markSupported()).isTrue();
    in.mark(Integer.MAX_VALUE);

    assertThat(in.skip(Long.MAX_VALUE)).isEqualTo(buf.writerIndex());
    assertThat(buf.isReadable()).isFalse();

    in.reset();
    assertThat(buf.readerIndex()).isEqualTo(0);

    assertThat(in.skip(4)).isEqualTo(4);
    assertThat(buf.readerIndex()).isEqualTo(4);

    in.reset();

    assertThat(in.readBoolean()).isTrue();
    assertThat(in.readBoolean()).isFalse();
    assertThat(in.readByte()).isEqualTo((byte) 42);
    assertThat(in.readUnsignedByte()).isEqualTo(224);

    byte[] tmp = new byte[13];
    in.readFully(tmp);

    String str = new String(tmp, "ISO-8859-1");
    assertThat(str).isEqualTo("Hello, World!");

//    in.readFully(tmp);
//    str = new String(tmp, "ISO-8859-1");
//    assertThat(str).isEqualTo("Hello, World!");

    assertThat(in.readChar()).isEqualTo('H');
    assertThat(in.readChar()).isEqualTo('e');
    assertThat(in.readChar()).isEqualTo('l');
    assertThat(in.readChar()).isEqualTo('l');
    assertThat(in.readChar()).isEqualTo('o');
    assertThat(in.readChar()).isEqualTo(',');
    assertThat(in.readChar()).isEqualTo(' ');
    assertThat(in.readChar()).isEqualTo('W');
    assertThat(in.readChar()).isEqualTo('o');
    assertThat(in.readChar()).isEqualTo('r');
    assertThat(in.readChar()).isEqualTo('l');
    assertThat(in.readChar()).isEqualTo('d');
    assertThat(in.readChar()).isEqualTo('!');

    assertThat(in.readDouble()).isEqualTo(42.0);
    assertThat(in.readFloat()).isEqualTo(42.0F);
    assertThat(in.readInt()).isEqualTo(42);
    assertThat(in.readLong()).isEqualTo(42L);
    assertThat(in.readShort()).isEqualTo((short) 42);
    assertThat(in.readUnsignedShort()).isEqualTo(49152);

    assertThat(in.readUTF()).isEqualTo("Hello, World!");
    assertThat(in.readLine()).isEqualTo("The first line");
    assertThat(in.readLine()).isEmpty();

    byte[] array = new byte[4];
    assertThat(in.read(array)).isEqualTo(4);
    assertThat(array).isEqualTo(new byte[] { 1, 2, 3, 4 });

    assertThat(in.read()).isEqualTo(-1);
    assertThat(in.read(tmp)).isEqualTo(-1);

    assertThatThrownBy(() -> in.readByte())
        .isInstanceOf(EOFException.class);

    assertThatThrownBy(() -> in.readFully(array, 0, -1))
        .isInstanceOf(IndexOutOfBoundsException.class);

    assertThatThrownBy(() -> in.readFully(array))
        .isInstanceOf(EOFException.class);

    in.close();

    assertThat(in.readBytes()).isEqualTo(buf.readerIndex());
  }

  @Test
  public void testReadLine() throws Exception {

    ByteBuf buf = Unpooled.buffer();
    ByteBufInputStream in = new ByteBufInputStream(buf);

    String s = in.readLine();
    assertThat(s).isNull();

    int charCount = 5;   //total chars in the string below without new line characters
    byte[] abc = "a\nb\nc\nd\ne".getBytes(CharsetUtil.UTF_8);

    buf.writeBytes(abc);
    in.mark(charCount);

    assertThat(in.readLine()).isEqualTo("a");
    assertThat(in.readLine()).isEqualTo("b");
    assertThat(in.readLine()).isEqualTo("c");
    assertThat(in.readLine()).isEqualTo("d");
    assertThat(in.readLine()).isEqualTo("e");

    assertThat(in.readLine()).isNull();

    in.reset();
    int count = 0;
    while (in.readLine() != null) {
      ++count;
      if (count > charCount) {
        fail("readLine() should habe returned null");
      }
    }

    in.close();
  }
}
