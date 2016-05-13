package nettybook.apiserver.domain.repository;

import nettybook.apiserver.domain.mapper.UserMapper;
import nettybook.apiserver.domain.model.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

@Repository
public class UserRepository {

  @Inject private UserMapper mapper;
  @Inject private SqlSessionTemplate template;


  public User findByPassword(String password) {
    return mapper.findByPassword(password);
  }

  public List<User> findAll() {
    return mapper.findAll();
  }

}
