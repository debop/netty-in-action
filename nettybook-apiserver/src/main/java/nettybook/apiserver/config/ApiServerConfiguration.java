package nettybook.apiserver.config;

import nettybook.apiserver.ApiServer;
import nettybook.apiserver.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Inject;
import java.net.InetSocketAddress;

@Configuration
@ComponentScan(basePackageClasses = { ApiServerSetting.class, UserService.class, ApiServer.class })
@Import({ MyBatisConfiguration.class })
public class ApiServerConfiguration {

  @Inject ApiServerSetting apiServerSetting;

  @Bean
  public InetSocketAddress tcpPort() {
    return new InetSocketAddress(apiServerSetting.getTcpPort());
  }
}
