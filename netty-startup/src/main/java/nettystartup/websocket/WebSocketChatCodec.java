package nettystartup.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nettystartup.chat.ChatMessage;

import java.util.List;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class WebSocketChatCodec extends MessageToMessageCodec<TextWebSocketFrame, ChatMessage> {

  @Override
  protected void encode(ChannelHandlerContext ctx, ChatMessage msg, List<Object> out) throws Exception {
    out.add(new TextWebSocketFrame(msg.toString()));
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
    out.add(ChatMessage.parse(msg.text()));
  }
}
