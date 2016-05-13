package nettybook.apiserver.core;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonClient;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
public abstract class ApiRequestTemplate implements ApiRequest {

  @Inject private RedissonClient redisson;

  protected Map<String, String> reqData;
  protected JsonObject apiResult;

  public ApiRequestTemplate(Map<String, String> reqData) {
    this.reqData = reqData;
    this.apiResult = new JsonObject();

    log.info("Request Data: {}", reqData);
  }

  @Override
  public void executeService() {
    try {
      this.requestParamValidation();
      this.service();
    } catch (RequestParamException pe) {
      log.error("요청정보 에러", pe);
      this.apiResult.addProperty("resultCode", "405");
    } catch (ServiceException se) {
      log.error("요청정보 처리 실패", se);
      this.apiResult.addProperty("resultCode", "501");
    }
  }

  @Override
  public void requestParamValidation() throws RequestParamException {
    if (getClass().getClasses().length == 0) {
      return;
    }
  }

  public final <T extends Enum<T>> T fromValue(Class<T> paramClass, String paramValue) {
    if (paramValue == null || paramClass == null) {
      throw new IllegalArgumentException("There is no value with name + '" + paramValue + "' in Enum ");
    }
    for (T param : paramClass.getEnumConstants()) {
      if (Objects.equals(param.name(), paramValue)) {
        return param;
      }
    }

    throw new IllegalArgumentException("There is no value with name + '" + paramValue + "' in Enum ");
  }

}
