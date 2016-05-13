package nettybook.apiserver.service;

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
 * API Token 을 삭제합니다
 */
@Service("tokenExpirer")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class TokenExpirer extends ApiRequestTemplate {

  public TokenExpirer() {}
  public TokenExpirer(Map<String, String> reqData) {
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
    String tokenKey = reqData.get("token");

    log.debug("토큰을 삭제합니다... key={}", tokenKey);

    if (StringEx.isNotEmpty(tokenKey)) {
      RBucket<String> bucket = getRedisson().getBucket(tokenKey);
      bucket.delete();

      apiResult.addProperty("resultCode", "200");
      apiResult.addProperty("message", "success");
      apiResult.addProperty("token", tokenKey);

      log.debug("토큰 정보를 삭제했습니다. apiResult={}", apiResult);
    }
  }
}
