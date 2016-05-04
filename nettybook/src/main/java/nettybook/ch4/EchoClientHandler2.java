package nettybook.ch4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class EchoClientHandler2 extends SimpleChannelInboundHandler<ByteBuf> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    String sendMessage = "Hello netty";

    ByteBuf msg = Unpooled.copiedBuffer(sendMessage, CharsetUtil.UTF_8);

    log.debug("전송한 문자열 [{}]", sendMessage);
    ctx.writeAndFlush(msg);
  }
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    String readMessage = msg.toString(CharsetUtil.UTF_8);
    log.debug("수신한 문자열 = [{}]", readMessage);
  }
}
