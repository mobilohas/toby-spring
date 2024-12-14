package org.mobilohas.bell.ch3.user.dao;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.mobilohas.bell.ch3.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDao {

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private RowMapper<User> userMapper = (rs, rowNum) -> new User(
      rs.getString("id"),
      rs.getString("name"),
      rs.getString("password"));

  public UserDao() {
  }

  public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void add(User user) throws SQLException {
    final String query = "insert into users(id, name, password) values(?,?,?)";
    jdbcTemplate.update(query, user.getId(), user.getName(), user.getPassword());
  }

  public void deleteAll() throws SQLException {
    jdbcTemplate.update("delete from users");
  }

  public User get(String id) throws SQLException {
    return jdbcTemplate.queryForObject("select * from users where id = ?",
        new Object[]{id}, // SQL에 바인딩할 파라미터 값, 가변인자 대신 배열을 사용한다.
        userMapper);
  }

  public int getCount() throws SQLException {
    return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
  }

  public List<User> getAll() {
    return jdbcTemplate.query("select * from users", userMapper);
  }
}
