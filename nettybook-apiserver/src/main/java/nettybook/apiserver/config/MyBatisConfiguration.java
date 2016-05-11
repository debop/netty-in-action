package nettybook.apiserver.config;

import kesti4j.config.servers.DatabaseSetting;
import kesti4j.data.JdbcDrivers;
import kesti4j.data.mybatis.config.AbstractFlywayMyBatisConfiguration;
import nettybook.apiserver.domain.mapper.UserMapper;
import nettybook.apiserver.domain.repository.UserRepository;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { UserRepository.class })
@MapperScan(basePackageClasses = { UserMapper.class })
public class MyBatisConfiguration extends AbstractFlywayMyBatisConfiguration {

  @Override
  protected boolean cleanDatabaseForTest() {
    // 테스트 시에는 true, 운영 시에는 항상 false 를 해야 합니다 (profile 기반으로 하면 더 좋지요)ㄴ
    return true;
  }

  @Override
  protected DatabaseSetting getDatabaseSetting() {
    return DatabaseSetting.builder()
                          .driverClass(JdbcDrivers.DRIVER_CLASS_H2)
                          .jdbcUrl("jdbc:h2:file:./users")
                          .username("sa")
                          .build();
  }

  @Override
  protected void setupSqlSessionFactory(SqlSessionFactoryBean sf) {
    super.setupSqlSessionFactory(sf);
    // sf.setTypeHandlersPackage("");
  }

  @Override
  protected String getMyBatisConfigPath() {
    return "";
    //return "classpath:mybatis/mybatis-config.xml";
  }
  @Override
  protected String getMyBatisMapperPath() {
    return "";
//    return "classpath:mybatis/mappers/*.xml";
  }
}
