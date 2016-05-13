package nettybook.apiserver.domain;

import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.AbstractApiServerTest;
import nettybook.apiserver.domain.model.User;
import nettybook.apiserver.domain.repository.UserRepository;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UserRepositoryTest extends AbstractApiServerTest {

  @Inject UserRepository userRepo;

  @Test
  public void testConfiguration() {
    assertThat(userRepo).isNotNull();
  }

  @Test
  public void testFindByPassword() {
    User user = userRepo.findByPassword("emma");
    assertThat(user).isNotNull();
    assertThat(user.getUserName()).isEqualTo("엠마");
  }

  @Test
  public void testFindAll() {
    List<User> users = userRepo.findAll();
    assertThat(users).isNotEmpty();
  }
}
