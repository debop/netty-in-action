package nettybook.telnet.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.net.InetSocketAddress;

@Configuration
@ComponentScan(basePackageClasses = { TelnetServer.class })
public class TelnetServerConfiguration {

  @Inject
  TelnetServerSetting telnetSetting;

  @Bean(name = "tcpSocketAddress")
  public InetSocketAddress tcpSocketAddress() {
    return new InetSocketAddress(telnetSetting.getTcpPort());
  }
}
