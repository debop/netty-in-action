package nettybook.telnet.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TelnetServerApplication { // implements CommandLineRunner {

//  @Inject TelnetServer server;

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(TelnetServerApplication.class, args);

    TelnetServer server = context.getBean(TelnetServer.class);
    server.start();
  }

//  @Override
//  public void run(String... args) throws Exception {
//    server.start();
//  }
}
