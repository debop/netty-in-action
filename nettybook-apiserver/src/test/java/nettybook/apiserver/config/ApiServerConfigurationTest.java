package nettybook.apiserver.config;

import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.ApiServer;
import nettybook.apiserver.domain.repository.UserRepository;
import nettybook.apiserver.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
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
}
