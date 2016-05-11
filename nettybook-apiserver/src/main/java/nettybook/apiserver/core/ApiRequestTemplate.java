package nettybook.apiserver.core;

import com.google.gson.JsonObject;
import kesti4j.core.NotImplementedException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public abstract class ApiRequestTemplate implements ApiRequest {

  protected Map<String, String> reqData;
  protected JsonObject apiResult;

  protected ApiRequestTemplate(Map<String, String> reqData) {
    this.reqData = reqData;
    this.apiResult = new JsonObject();

    log.info("Request Data: {}", reqData);
  }

  @Override
  public void requestParamValidation() throws RequestParamException {
    throw new NotImplementedException("구현 중");
  }
  @Override
  public void service() throws ServiceException {
    throw new NotImplementedException("구현 중");
  }
  @Override
  public void executeService() {
    throw new NotImplementedException("구현 중");
  }

}
