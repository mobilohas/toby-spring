package org.mobilohas.bell.ch1.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class CountingDaoFactory {

  @Bean
  public UserDao userDao() {
    return new UserDao(dataSource());
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new CountingConnectionMaker(realConnectionMaker());
  }

  @Bean
  public ConnectionMaker realConnectionMaker() {
    return new DConnectionMaker();
  }

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost:3307/spring");
    dataSource.setUsername("root");
    dataSource.setPassword("qwer1234");
    return dataSource;
  }
}
