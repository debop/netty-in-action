package nia.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import nia.utils.ByteBufEx;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final ByteBuf in = (ByteBuf) msg;
        final String s = ByteBufEx.toUtf8String(in);
        log.debug("Server received: {}", s.trim());

        // echo
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        // 이것은 한번만 echo 를 수행하고, 접속을 끊어버린다.
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//           .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("echo server에 예외가 발생했습니다.", cause);
        ctx.close();
        log.warn("예외가 발생하여 echo server ChannelHandlerContext를 닫습니다");
    }
}
