package org.mobilohas.bell.ch3.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.mobilohas.bell.ch3.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
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

//  public User get(String id) throws SQLException {
//    Connection c = dataSource.getConnection();
//
//    PreparedStatement ps = c.prepareStatement(
//        "select * from users where id = ?");
//    ps.setString(1, id);
//
//    ResultSet rs = ps.executeQuery();
//
//    User user = null;
//    if (rs.next()) {
//      user = new User();
//      user.setId(rs.getString("id"));
//      user.setName(rs.getString("name"));
//      user.setPassword(rs.getString("password"));
//    }
//
//    rs.close();
//    ps.close();
//    c.close();
//
//    if (user == null) {
//      throw new EmptyResultDataAccessException(1);
//    }
//
//    return user;
//  }

  public int getCount() throws SQLException {
    return jdbcTemplate.query(
        con -> con.prepareStatement("select count(*) from users"),
        rs -> {
          rs.next();
          return rs.getInt(1);
        });
  }
}
