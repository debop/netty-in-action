package nettybook.apiserver.service;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.AbstractApiServerTest;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.RedissonClient;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TokenServiceTest extends AbstractApiServerTest {

  @Inject ApplicationContext appContext;
  @Inject RedissonClient redisson;

  @Resource(name = "tokenIssue") TokenIssue tokenIssue;
  @Resource(name = "tokenVerify") TokenVerify tokenVerify;
  @Resource(name = "tokenExpirer") TokenExpirer tokenExpirer;

  Map<String, String> reqData = Maps.mutable.of();

  @Before
  public void setup() {
    assertThat(redisson).isNotNull();
    redisson.getKeys().flushdb();

    reqData.clear();
    reqData.put("userNo", "5");
    reqData.put("password", "emma");

    tokenIssue = (TokenIssue) appContext.getBean("tokenIssue", reqData);
    tokenVerify = (TokenVerify) appContext.getBean("tokenVerify", reqData);
    tokenExpirer = (TokenExpirer) appContext.getBean("tokenExpirer", reqData);
  }

  @After
  public void cleanup() {

    log.debug("모든 토큰 정보를 삭제합니다!!!");
    redisson.getKeys().flushdb();
  }

  @Test
  public void testInjection() {
    assertThat(redisson).isNotNull();

    assertThat(tokenIssue).isNotNull();
    assertThat(tokenVerify).isNotNull();
    assertThat(tokenExpirer).isNotNull();
  }

  @Test
  public void testTokenIssue() {

    tokenIssue = (TokenIssue) appContext.getBean("tokenIssue", reqData);
    assertThat(tokenIssue).isNotNull();

    tokenIssue.requestParamValidation();
    tokenIssue.service();

    JsonObject json = tokenIssue.getApiResult();
    assertThat(json.get("resultCode").getAsString()).isEqualTo("200");


    String token = json.get("token").getAsString();
    reqData.put("token", token);
    tokenVerify = (TokenVerify) appContext.getBean("tokenVerify", reqData);
    tokenVerify.requestParamValidation();
    tokenVerify.service();

    json = tokenVerify.getApiResult();
    assertThat(json.get("resultCode").getAsString()).isEqualTo("200");


    tokenExpirer = (TokenExpirer) appContext.getBean("tokenExpirer", reqData);
    tokenExpirer.requestParamValidation();
    tokenExpirer.service();
    json = tokenExpirer.getApiResult();
    assertThat(json.get("resultCode").getAsString()).isEqualTo("200");
  }
}
