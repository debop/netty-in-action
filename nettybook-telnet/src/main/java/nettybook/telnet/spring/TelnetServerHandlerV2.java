package nettybook.telnet.spring;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.Date;

@Slf4j
@ChannelHandler.Sharable
public class TelnetServerHandlerV2 extends SimpleChannelInboundHandler<String> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.write("환영합니다. " + InetAddress.getLocalHost().getHostName() + "에 접속하셨습니다.\r\n");
    ctx.write("현재 시각은 " + new Date() + " 입니다.\r\n");
    ctx.flush();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.debug("Channel 을 닫았습니다.");
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    log.debug("수신: {}", msg);

    String response;
    boolean close = false;

    if (msg.isEmpty()) {
      response = "명령을 입력해 주세요.\r\n";
    } else if ("bye".equals(msg.toLowerCase())) {
      response = "좋은 하루되세요!\r\n";
      close = true;
    } else {
      response = "입력하신 명령이 " + msg + " 입니까?\r\n";
    }

    ChannelFuture future = ctx.write(response);
    log.debug("전송: {}", response);

    if (close) {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("예외 발생", cause);
  }
}