package nia.chapter06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * InboundExceptionHandler
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("error occurred", cause);
        ctx.close();
    }
}
