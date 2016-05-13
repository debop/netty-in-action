package nettybook.apiserver.service;

import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.AbstractApiServerTest;
import nettybook.apiserver.core.KeyMaker;
import org.junit.Test;
import org.redisson.RedissonClient;
import org.redisson.core.RBucket;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TokenKeyMakerTest extends AbstractApiServerTest {

  @Inject RedissonClient redisson;

  @Test
  public void testConfiguration() {
    assertThat(redisson).isNotNull();
  }

  @Test
  public void testRedisConnection() {
    RBucket<String> bucket = redisson.getBucket("test");
    bucket.set("key");

    assertThat(bucket.isExists()).isTrue();
    assertThat(bucket.get()).isEqualTo("key");
    assertThat(bucket.delete()).isTrue();
  }

  @Test
  public void testTokenKeyMaker() {


    KeyMaker keyMaker = new TokenKeyMaker("shbae@gmail.com", System.currentTimeMillis() / 1000);
    String key = keyMaker.getKey();
    assertThat(key).isNotEmpty();
    log.debug("key={}", key);
  }
}
