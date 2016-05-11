package nettybook.telnet.tests;

import kesti4j.core.utils.StringEx;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.factory.Sets;
import org.joda.time.DateTime;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

@Slf4j
public class ResponseGeneratorV2 {

  private static final Set<String> EXIT_MESSAGES = Sets.mutable.with("bye");

  private String request;

  public ResponseGeneratorV2(String request) {
    this.request = request;
  }

  public final String response() {
    String response = null;

    if (StringEx.isEmpty(request)) {
      response = "명령을 입력해 주세요.\r\n";
    } else if (isClose()) {
      response = "좋은 하루 되세요\r\n";
    } else {
      response = "입력하신 명령이 '" + request + "' 입니까?\r\n";
    }

    return response;
  }

  public boolean isClose() {
    return StringEx.isNotEmpty(this.request) &&
           EXIT_MESSAGES.contains(this.request.toLowerCase());
  }

  public static String makeHello() throws UnknownHostException {
    StringBuilder builder = new StringBuilder();

    builder.append("환영합니다. ")
           .append(InetAddress.getLocalHost().getHostName())
           .append("에 접속하셨습니다.\r\n")
           .append("현재 시각은 ").append(DateTime.now().toString())
           .append(" 입니다.\r\n");

    return builder.toString();
  }
}
