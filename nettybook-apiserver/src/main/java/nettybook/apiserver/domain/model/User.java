package nettybook.apiserver.domain.model;

import kesti4j.core.AbstractValueObject;
import kesti4j.core.ToStringHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends AbstractValueObject {

  private Integer userNo;
  private String userId;
  private String userName;
  private String password;

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("userNo", userNo)
                .add("userId", userId)
                .add("userName", userName)
                .add("password", password);
  }
}
