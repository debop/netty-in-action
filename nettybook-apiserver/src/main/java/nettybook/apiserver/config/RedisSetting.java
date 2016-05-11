package nettybook.apiserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
@Getter
@Setter
public class RedisSetting {

  public String host = "localhost";

  public int port = 6379;

  public String getAddress() {
    return host + ":" + port;
  }
}
