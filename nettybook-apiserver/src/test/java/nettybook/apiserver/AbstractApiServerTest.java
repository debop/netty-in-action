package nettybook.apiserver;

import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.config.ApiServerConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ApiServerConfiguration.class })
public abstract class AbstractApiServerTest {
}
