package nettybook.apiserver.config;

import nettybook.apiserver.ApiServer;
import nettybook.apiserver.service.UserService;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
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
  @Inject RedisSetting redisSetting;

  @Bean
  public InetSocketAddress tcpPort() {
    return new InetSocketAddress(apiServerSetting.getTcpPort());
  }


  @Bean
  public RedissonClient redissonClient() {
    Config cfg = new Config();
    cfg.useSingleServer()
       .setAddress(redisSetting.getAddress())
       .setConnectionPoolSize(10)
       .setConnectionMinimumIdleSize(1);

    return Redisson.create(cfg);
  }
}
