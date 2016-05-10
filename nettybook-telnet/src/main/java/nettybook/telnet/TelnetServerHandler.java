package nettybook.telnet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.Date;

import static io.netty.channel.ChannelHandler.Sharable;

@Slf4j
@Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

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
    String response;
    boolean close = false;

    if (msg.isEmpty()) {
      response = "명령을 입력해 주세요.\r\n";
    } else if ("bye".equals(msg.toLowerCase())) {
      response = "좋은 하루되세요!\r\n";
      close = true;
    } else {
      response = "입력하신 명령이 " + msg + "입니까?\r\n";
    }

    ChannelFuture future = ctx.write(response);

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
