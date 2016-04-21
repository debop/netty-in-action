package nia.chapter06;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * WriteHandler
 *
 * @author sunghyouk.bae@gmail.com
 */
public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    public void send(String msg) {
        ctx.writeAndFlush(msg);
    }
}
