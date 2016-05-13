package nettybook.apiserver.domain.mapper;

import lombok.NonNull;
import nettybook.apiserver.domain.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {


  @Select("SELECT * FROM USERS WHERE Password = #{password}")
  User findByPassword(@NonNull @Param("password") String password);

  @Select("SELECT * FROM USERS")
  List<User> findAll();
}
