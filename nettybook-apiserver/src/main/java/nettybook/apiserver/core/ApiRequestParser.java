package nettybook.apiserver.core;

import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import kesti4j.core.NotImplementedException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;

import java.util.Map;
import java.util.Set;

/**
 * HTTP 요청정보를 파싱합니다.
 */
@Slf4j
public class ApiRequestParser extends SimpleChannelInboundHandler<FullHttpMessage> {

  private HttpRequest request;
  private JsonObject apiResult;

  private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

  private HttpPostRequestDecoder decoder;

  private Map<String, String> reqData = new UnifiedMapWithHashingStrategy<>(HashingStrategies.identityStrategy());

  private static final Set<String> usingHeader = Sets.mutable.with("token", "email");


  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    log.info("요청 처리 완료");
    ctx.flush();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
    throw new NotImplementedException("구현 중");
  }

  private void reset() {
    request = null;
  }

  private void readPostData() {
    throw new NotImplementedException("구현 중");
  }

  private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
    throw new NotImplementedException("구현 중");
  }

  private static void send100Condition(ChannelHandlerContext ctx) {
    throw new NotImplementedException("구현 중");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("요청 처리 중 예외 발생", cause);
    ctx.close();
  }
}
