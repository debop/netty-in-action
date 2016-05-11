package nettybook.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ApiServerMain {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(ApiServerMain.class, args);

    ApiServer apiServer = context.getBean(ApiServer.class);
    apiServer.start();
  }
}
