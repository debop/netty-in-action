package nettybook.telnet.tests;

import kesti4j.core.utils.StringEx;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class ResponseGenerator {

  private String request;

  public ResponseGenerator(String request) {
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
    return StringEx.isNotEmpty(this.request) && "bye".equals(this.request.toLowerCase());
  }

  @SneakyThrows({ UnknownHostException.class })
  public static String makeHello() {
    StringBuilder builder = new StringBuilder();

    builder.append("환영합니다. ")
           .append(InetAddress.getLocalHost().getHostName())
           .append("에 접속하셨습니다.\r\n")
           .append("현재 시각은 ").append(DateTime.now().toString())
           .append(" 입니다.\r\n");

    return builder.toString();
  }
}
