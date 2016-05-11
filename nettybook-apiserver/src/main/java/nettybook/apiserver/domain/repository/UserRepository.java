package nettybook.apiserver.domain.repository;

import nettybook.apiserver.domain.mapper.UserMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

@Repository
public class UserRepository {

  @Inject private UserMapper mapper;
  @Inject private SqlSessionTemplate template;

}
