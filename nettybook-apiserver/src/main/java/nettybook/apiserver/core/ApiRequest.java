package nettybook.apiserver.core;

import com.google.gson.JsonObject;

public interface ApiRequest {

  public void requestParamValidation() throws RequestParamException;

  public void service() throws ServiceException;

  public void executeService();

  public JsonObject getApiResult();
}
