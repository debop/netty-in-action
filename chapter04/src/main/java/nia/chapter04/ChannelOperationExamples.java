package nia.chapter04;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;

/**
 * ChannelOperationExamples
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class ChannelOperationExamples {

    public static void writingToChannel() {
        Channel channel = new EmbeddedChannel(new ChannelHandlerAdapter() {});

        ByteBuf buf = ByteBufEx.toByteBuf("your data");
        ChannelFuture cf = channel.write(buf);

        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("Write successful");
                } else {
                    log.error("Write error");
                    future.cause().printStackTrace();
                }
            }
        });
    }
}
