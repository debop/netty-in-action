package nia.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kesti4j.core.utils.StringEx;
import lombok.NonNull;

/**
 * @author sunghyouk.bae@gmail.com
 */
public final class ByteBufEx {

    private ByteBufEx() {}

    public static ByteBuf toByteBuf(@NonNull String str) {
        return Unpooled.copiedBuffer(StringEx.toUtf8Bytes(str)).retain();
    }

    public static String toUtf8String(@NonNull ByteBuf buf) {
        byte[] b = new byte[buf.readableBytes()];
        buf.getBytes(0, b, 0, b.length);

        return StringEx.toUtf8String(b);
    }
}
