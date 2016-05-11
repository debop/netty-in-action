package nettybook.apiserver.core;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("notFound")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultApiRequest extends ApiRequestTemplate {

  public DefaultApiRequest(Map<String, String> reqData) {
    super(reqData);
  }

  @Override
  public void service() throws ServiceException {
    this.getApiResult().addProperty("resultCode", "404");
  }
}
