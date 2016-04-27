package nettystartup.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<ChatMessage> {

  private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  static final AttributeKey<String> nickAttr = AttributeKey.newInstance("nickname");
  static final NicknameProvider nicknameProvider = new NicknameProvider();

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    // Tricky: 이미 channel이 active인 상황에서
    // 동적으로 이 핸들러가 등록될 때에는 channelActive가 불리지않습니다.
    if (ctx.channel().isActive()) {
      helo(ctx.channel());
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    helo(ctx.channel());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    channels.remove(ctx.channel());
    String nick = nickname(ctx);
    channels.writeAndFlush(M("LEFT", nick));
    nicknameProvider.release(nick);
  }

  private void helo(Channel ch) {
    if (nickname(ch) != null)
      return; // already done;

    String nick = nicknameProvider.reserve();
    if (nick == null) {
      ch.writeAndFlush(M("ERR", "sorry, no more names for you"));
    } else {
      bindNickname(ch, nick);
      channels.forEach(c -> c.write(M("HAVE", nickname(c))));
      channels.writeAndFlush(M("JOIN", nick));
      channels.add(ch);
      ch.writeAndFlush(M("HELO", nick));
    }
  }
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
    log.debug("server read: {}", msg);

    String command = msg.getCommand();

    switch (command) {
      case "PING":
        ctx.write(M("PONG"));
        break;
      case "QUIT":
        ctx.writeAndFlush(M("BYE", nickname(ctx)))
           .addListener(ChannelFutureListener.CLOSE);
        break;
      case "SEND":
        channels.writeAndFlush(M("FROM", nickname(ctx), msg.getText()));
        break;
      case "NICK":
        changeNickname(ctx, msg);
        break;
      default:
        ctx.write(M("ERR", null, "unknown command -->" + command));
        break;
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("ERROR", cause);
    if (!ctx.channel().isActive()) {
      ctx.writeAndFlush(M("ERR", null, cause.getMessage()))
         .addListener(ChannelFutureListener.CLOSE);
    }
  }

  private void changeNickname(ChannelHandlerContext ctx, ChatMessage msg) {
    String newNick = msg.getText().replace(" ", "_").replace(":", "-");
    String prev = nickname(ctx);
    if (!newNick.equals(prev) && nicknameProvider.available(newNick)) {
      nicknameProvider.release(prev).reserve(newNick);
      bindNickname(ctx.channel(), newNick);
      channels.writeAndFlush(M("NICK", prev, newNick));
    } else {
      ctx.write(M("ERR", null, "couldn't change"));
    }
  }

  private ChatMessage M(String... args) {
    switch (args.length) {
      case 1:
        return new ChatMessage(args[0]);
      case 2:
        return new ChatMessage(args[0], args[1]);
      case 3:
        ChatMessage m = new ChatMessage(args[0], args[1]);
        m.setText(args[2]);
        return m;
      default:
        throw new IllegalArgumentException();
    }
  }

  private void bindNickname(Channel c, String nickname) {
    c.attr(nickAttr).set(nickname);
  }

  private String nickname(Channel c) {
    return c.attr(nickAttr).get();
  }

  private String nickname(ChannelHandlerContext ctx) {
    return nickname(ctx.channel());
  }
}
