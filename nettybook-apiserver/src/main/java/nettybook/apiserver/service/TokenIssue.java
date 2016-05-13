package nettybook.apiserver.service;

import com.google.gson.JsonObject;
import kesti4j.core.utils.StringEx;
import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.core.ApiRequestTemplate;
import nettybook.apiserver.core.KeyMaker;
import nettybook.apiserver.core.RequestParamException;
import nettybook.apiserver.core.ServiceException;
import nettybook.apiserver.domain.model.User;
import nettybook.apiserver.domain.repository.UserRepository;
import org.eclipse.collections.impl.factory.Maps;
import org.redisson.core.RBucket;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * API Key 를 발급합니다.
 */
@Service("tokenIssue")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TokenIssue extends ApiRequestTemplate {

  @Inject UserRepository userRepo;

  public TokenIssue() { super(Maps.mutable.of()); }

  public TokenIssue(Map<String, String> reqData) {
    super(reqData);
  }

  @Override
  public void requestParamValidation() throws RequestParamException {
    if (StringEx.isEmpty(reqData.get("userNo"))) {
      throw new RequestParamException("userNo 가 없습니다.");
    }
    if (StringEx.isEmpty(reqData.get("password"))) {
      throw new RequestParamException("password 가 없습니다.");
    }
  }

  @Override
  public void service() throws ServiceException {
    User user = userRepo.findByPassword(reqData.get("password"));

    if (user == null) {
      apiResult.addProperty("resultCode", "404");
    } else {

      final long ttl = 3 * 60 * 60;
      final long issueDate = System.currentTimeMillis() / 1000;

      JsonObject token = buildToken(user, issueDate, ttl);
      KeyMaker tokenKey = new TokenKeyMaker(user.getUserId(), issueDate);

      RBucket<String> bucket = getRedisson().getBucket(tokenKey.getKey());
      bucket.set(token.toString(), ttl, TimeUnit.SECONDS);

      apiResult.addProperty("resultCode", "200");
      apiResult.addProperty("message", "success");
      apiResult.addProperty("token", tokenKey.getKey());
    }
  }

  private JsonObject buildToken(User user, long issueDate, long ttl) {

    String email = user.getUserId();

    JsonObject token = new JsonObject();
    token.addProperty("issueDate", issueDate);
    token.addProperty("expireDate", issueDate + ttl);
    token.addProperty("email", user.getUserId());
    token.addProperty("userNo", user.getUserNo());

    return token;
  }
}
