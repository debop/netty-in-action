package nettybook.apiserver.service;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.AbstractApiServerTest;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TokenIssueTest extends AbstractApiServerTest {

  @Inject ApplicationContext appContext;

  @Resource(name = "tokenIssue") TokenIssue tokenIssue;

  Map<String, String> reqData = Maps.mutable.of();

  @Before
  public void setup() {
    reqData.clear();
    reqData.put("userNo", "5");
    reqData.put("password", "emma");

    tokenIssue = (TokenIssue) appContext.getBean("tokenIssue", reqData);
  }


  @Test
  public void testTokenIssue() {
    assertThat(tokenIssue).isNotNull();

    tokenIssue.requestParamValidation();
    tokenIssue.service();

    JsonObject json = tokenIssue.getApiResult();
    assertThat(json.get("resultCode").getAsString()).isEqualTo("200");
  }
}
