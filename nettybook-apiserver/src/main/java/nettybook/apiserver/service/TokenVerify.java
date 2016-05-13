package nettybook.apiserver.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kesti4j.core.utils.StringEx;
import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.core.ApiRequestTemplate;
import nettybook.apiserver.core.RequestParamException;
import nettybook.apiserver.core.ServiceException;
import org.redisson.core.RBucket;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 토큰 값을 검증합니다.
 */
@Service("tokenVerify")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TokenVerify extends ApiRequestTemplate {

  public TokenVerify() {}
  public TokenVerify(Map<String, String> reqData) {
    super(reqData);
  }

  @Override
  public void requestParamValidation() throws RequestParamException {
    super.requestParamValidation();

    if (StringEx.isEmpty(reqData.get("token"))) {
      throw new RequestParamException("token 이 없습니다.");
    }
  }

  @Override
  public void service() throws ServiceException {
    log.debug("사용자 토큰 정보를 검증합니다... reqData={}", reqData);

    String tokenKey = reqData.get("token");
    RBucket<String> bucket = getRedisson().getBucket(tokenKey);

    if (bucket.isExists()) {
      String tokenStr = bucket.get();
      log.debug("token 저장 정보 = {}", tokenStr);
      JsonObject token = new Gson().fromJson(tokenStr, JsonObject.class);

      apiResult.addProperty("resultCode", "200");
      apiResult.addProperty("message", "Success");
      apiResult.add("issueDate", token.get("issueDate"));
      apiResult.add("email", token.get("email"));
      apiResult.add("userNo", token.get("userNo"));

      log.debug("토큰 정보 검증에 성공했습니다. apiResult={}", apiResult);

    } else {
      log.warn("사용자 토큰 정보가 없습니다. tokenKey={}", tokenKey);

      apiResult.addProperty("resultCode", "404");
      apiResult.addProperty("message", "Fail");
    }

  }
}
