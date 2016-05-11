package nettybook.apiserver.service;

import nettybook.apiserver.domain.repository.UserRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Service("users")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserService {

  @Inject UserRepository userRepo;


}
