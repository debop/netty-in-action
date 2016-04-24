package nettystartup.chat.promise;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PromisingServerHandler extends SimpleChannelInboundHandler<String> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String line) throws Exception {
    log.debug("recieved: {}", line);
    log.debug("thread: {}", Thread.currentThread().getName());
    ctx.write(">" + line + "\n");

    Promise<String> p = ctx.executor().newPromise();
    new Thread(() -> {
      try {
        Thread.sleep(1000);
        log.debug("new Thread: {}", Thread.currentThread().getName());
        p.setSuccess("hello from " + Thread.currentThread().getName());
      } catch (InterruptedException ignored) {}
    }).start();

    p.addListener(f -> {
      log.debug("[{}] listener got: {}", Thread.currentThread().getName(), f.get());
    });
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("Error", cause);
    ctx.close();
  }
}
