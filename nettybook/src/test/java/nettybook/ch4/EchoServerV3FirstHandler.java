package nettybook.ch4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServerV3FirstHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf readMessage = (ByteBuf) msg;
    log.debug("channel read: {}", readMessage.toString(CharsetUtil.UTF_8));
    ctx.write(msg);
  }
}
