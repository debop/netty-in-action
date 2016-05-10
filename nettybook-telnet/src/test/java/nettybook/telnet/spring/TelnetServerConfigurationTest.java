package nettybook.telnet.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetSocketAddress;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TelnetServerConfiguration.class })
public class TelnetServerConfigurationTest {

  @Inject
  @Named("tcpSocketAddress")
  InetSocketAddress address;

  @Inject TelnetServerSetting telnetSetting;

  @Inject TelnetServer telnetServer;

  @Test
  public void testConfiguration() {
    assertThat(telnetSetting).isNotNull();

    assertThat(telnetSetting.getBaseThreadCount()).isEqualTo(1);
    assertThat(telnetSetting.getWorkerThreadCount()).isEqualTo(10);
    assertThat(telnetSetting.getTcpPort()).isEqualTo(8023);

    assertThat(address).isNotNull();

    assertThat(telnetServer).isNotNull();
  }
}
