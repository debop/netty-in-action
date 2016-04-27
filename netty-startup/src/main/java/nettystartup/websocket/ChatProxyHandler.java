package nettystartup.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettystartup.chat.ChatMessage;

public class ChatProxyHandler extends SimpleChannelInboundHandler<ChatMessage> {

  private final Channel wsChannel;

  public ChatProxyHandler(Channel wsChannel) {
    this.wsChannel = wsChannel;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
    // Nothing to do.
  }
}
