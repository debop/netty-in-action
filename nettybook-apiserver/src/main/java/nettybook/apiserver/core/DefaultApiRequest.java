package nettybook.apiserver.core;

import java.util.Map;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class DefaultApiRequest extends ApiRequestTemplate {

  public DefaultApiRequest(Map<String, String> reqData) {
    super(reqData);
  }

  @Override
  public void service() throws ServiceException {
    this.getApiResult().addProperty("resultCode", "404");
  }
}
