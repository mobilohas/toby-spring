package org.mobilohas.bell.ch1.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.mobilohas.bell.ch1.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDao {

  private JdbcTemplate jdbcTemplate;

  private RowMapper<User> userMapper = (rs, i) -> {
    User user = new User();
    user.setId(rs.getString("id"));
    user.setName(rs.getString("name"));
    user.setPassword(rs.getString("password"));
    return user;

  };

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void add(User user) {
    this.jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)", user.getId(),
        user.getName(), user.getPassword());
  }

  public List<User> getAll() {
    return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
  }

  public User get(String id) {
    return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", new Object[]{id},
        this.userMapper);
  }

  public void deleteAll() {
    this.jdbcTemplate.update("delete from users");
  }


  public int getCount() {
    return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
  }
}
