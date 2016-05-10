package nettybook.telnet.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telnet")
@Getter
@Setter
public class TelnetServerSetting {

  private int baseThreadCount;
  private int workerThreadCount;
  private int tcpPort;
}
