package nettystartup.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class ChatMessageCodec extends MessageToMessageCodec<String, ChatMessage> {

  @Override
  protected void encode(ChannelHandlerContext ctx, ChatMessage msg, List<Object> out) throws Exception {
    out.add(msg.toString() + "\n");
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
    out.add(ChatMessage.parse(msg));
  }
}
