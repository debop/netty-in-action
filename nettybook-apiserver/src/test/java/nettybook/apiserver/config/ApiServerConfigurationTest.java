package nettybook.apiserver.config;

import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.ApiServer;
import nettybook.apiserver.domain.repository.UserRepository;
import nettybook.apiserver.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.redisson.RedissonClient;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ApiServerConfiguration.class })
public class ApiServerConfigurationTest {

  @Inject SqlSessionTemplate session;
  @Inject UserRepository userRepo;
  @Inject UserService userService;

  @Inject ApiServer apiServer;

  @Inject ApiServerSetting apiServerSetting;

  @Inject RedisSetting redisSetting;
  @Inject RedissonClient redissonClient;

  @Test
  public void testConfiguration() {
    assertThat(session).isNotNull();
    assertThat(userRepo).isNotNull();
    assertThat(userService).isNotNull();
  }


  @Test
  public void testApiServer() {
    assertThat(apiServer).isNotNull();
  }

  @Test
  public void testApiServerSetting() {
    assertThat(apiServerSetting).isNotNull();
    assertThat(apiServerSetting.getBaseThreadCount()).isEqualTo(1);
  }

  @Test
  public void testRedisConfiguration() {
    assertThat(redisSetting).isNotNull();
    assertThat(redisSetting.getHost()).isEqualTo("localhost");

    assertThat(redissonClient).isNotNull();
  }
}
