package nettystartup.time;

import lombok.extern.slf4j.Slf4j;
import nettystartup.NettyStartupUtil;

/**
 * TimeServer
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class TimeServer {

  public static void main(String[] args) throws Exception {
    NettyStartupUtil.runServer(8022, new TimeServerHandler());
  }
}
