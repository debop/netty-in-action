package nettystartup.websocket;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import nettystartup.chat.ChatServerHandler;


public class WebChatHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    ctx.pipeline()
       .addLast(new WebSocketChatCodec())
       .addLast(new ChatServerHandler());
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
    if (frame instanceof CloseWebSocketFrame) {
      ctx.channel()
         .writeAndFlush(frame.retain())
         .addListener(ChannelFutureListener.CLOSE);
      return;
    }

    if (frame instanceof PingWebSocketFrame) {
      ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
      return;
    }

    if (!(frame instanceof TextWebSocketFrame)) {
      throw new UnsupportedOperationException(frame.getClass().getName() + " frame types not supported");
    }

    // 이 WebChatHandler는  TextWebSocketFrame만 다음 핸들러에 위임합니다.
    // SimpleInboundChannelHandler<T> 는 기본적으로 release() 처리가 들어있기 때문에
    // 아래 코드는 retain() 이 필요합니다.
    ctx.fireChannelRead(frame.retain());
  }

  /**
   * 채널 파이프라인에서 현재 핸들러가 등록된 이름을 구합니다.
   * 이 이름을 기준으로 앞뒤에 다른 핸들러를 추가할 수 있습니다.
   *
   * @param ctx
   * @return
   */
  private String findHandlerName(ChannelHandlerContext ctx) {
    final String[] result = new String[1];
    ctx.pipeline().toMap().forEach((name, handler) -> {
      if (handler.getClass().equals(this.getClass())) {
        result[0] = name;
      }
    });
    return result[0];
  }
}
