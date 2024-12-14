package org.mobilohas.bell.ch3.user.dao;

import org.mobilohas.bell.ch3.user.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class UserMapper {

  @Bean
  public RowMapper<User> userMapper() {
    return (rs, rowNum) -> new User(
        rs.getString("id"),
        rs.getString("name"),
        rs.getString("password"));
  }
}
