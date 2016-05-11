package nettybook.apiserver.core;

import kesti4j.core.NotImplementedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * URL 기존으로 서비스를 전달합니다
 */
@Component
@Slf4j
public class ServiceDispatcher {

  private static ApplicationContext appContext;

  public void init(ApplicationContext appContext) {
    ServiceDispatcher.appContext = appContext;
  }

  public static ApiRequest dispatch(Map<String, String> requestMap) {
    throw new NotImplementedException("구현 중");
  }
}
