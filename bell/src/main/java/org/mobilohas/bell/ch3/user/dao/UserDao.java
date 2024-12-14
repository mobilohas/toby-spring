package org.mobilohas.bell.ch3.user.dao;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.mobilohas.bell.ch3.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDao {

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

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
        (rs, i) -> { // ResultSet 한 로우의 결과를 오브젝트에 매핑해주는 RowMapper 콜백
          User user = new User();
          user.setId(rs.getString("id"));
          user.setName(rs.getString("name"));
          user.setPassword(rs.getString("password"));
          return user;
        });
  }

  public int getCount() throws SQLException {
    return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
  }
}
