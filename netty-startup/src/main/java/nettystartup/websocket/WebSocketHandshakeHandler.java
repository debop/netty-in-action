package nettystartup.websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class WebSocketHandshakeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private final String wsPath;
  private ChannelHandler wsHandler;

  public WebSocketHandshakeHandler(String wsPath, ChannelHandler wsHandler) {
    super(false);
    this.wsPath = wsPath;
    this.wsHandler = wsHandler;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
    if (wsPath.equals(req.getUri())) {
      // wsPath 로 온 요청은 WebSocket 업그레이드 시도
      handshakeWebSocket(ctx, req);
    } else {
      // 그 외의 path 는 다음 핸들러로 위임 (auto release 가 false 이므로 retain() 을 호출할 필요는 없다)
      ctx.fireChannelRead(req);
    }
  }

  private void handshakeWebSocket(ChannelHandlerContext ctx, FullHttpRequest req) {
    try {
      WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(wsPath, null, true);
      WebSocketServerHandshaker h = wsFactory.newHandshaker(req);
      if (h == null) {
        WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
      } else {
        h.handshake(ctx.channel(), req).addListener((ChannelFuture f) -> {
          // handshaking 이 완료되면, 원하는 Handler 로 교체합니다.
          ChannelPipeline p = f.channel().pipeline();
          p.replace(WebSocketHandshakeHandler.class, "wsHandler", wsHandler);
        });
      }
    } finally {
      req.release();
    }
  }
}
