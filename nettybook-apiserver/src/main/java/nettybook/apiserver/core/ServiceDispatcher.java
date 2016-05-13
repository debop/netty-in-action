package nettybook.apiserver.core;

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
    log.trace("requestMap={}", requestMap);

    String serviceUri = requestMap.get("REQUEST_URI");

    if (serviceUri == null) {
      return (ApiRequest) appContext.getBean("notFound", requestMap);
    }


    if (serviceUri.startsWith("/tokens")) {
      String httpMethod = requestMap.get("REQUEST_METHOD");
      String beanName = getBeanName(httpMethod);

      return (ApiRequest) appContext.getBean(beanName, requestMap);
    }

    return null;
  }

  private static String getBeanName(String httpMethod) {
    switch (httpMethod) {
      case "POST":
        return "tokenIssue";
      case "DELETE":
        return "tokenExpirer";
      case "GET":
        return "tokenVerify";

      default:
        return "notFound";
    }
  }
}
