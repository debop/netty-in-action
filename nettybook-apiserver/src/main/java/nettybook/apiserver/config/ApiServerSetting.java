package nettybook.apiserver.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apiserver")
@Getter
@Setter
public class ApiServerSetting {

  private int baseThreadCount;
  private int workerThreadCount;
  private int tcpPort;

}
