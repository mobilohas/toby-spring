package org.mobilohas.bell.ch3.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.mobilohas.bell.ch3.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao {

  private JdbcContext jdbcContext;

  public UserDao() {
  }

  public UserDao(final JdbcContext jdbcContext) {
    this.jdbcContext = jdbcContext;
  }

  public void setJdbcContext(final JdbcContext jdbcContext) {
    this.jdbcContext = jdbcContext;
  }

  public void add(User user) throws SQLException {
    final String query = "insert into users(id, name, password) values(?,?,?)";
    jdbcContext.executeSql(query, user.getId(), user.getName(), user.getPassword());
  }

  public void deleteAll() throws SQLException {
    jdbcContext.executeSql("delete from users");
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

//  public int getCount() throws SQLException {
//    Connection c = null;
//    PreparedStatement ps = null;
//    ResultSet rs = null;
//
//    try {
//      c = dataSource.getConnection();
//      ps = c.prepareStatement("select count(*) from users");
//      rs = ps.executeQuery();
//      rs.next();
//      return rs.getInt(1);
//    } catch (SQLException e) {
//      throw e;
//    } finally {
//      if (rs != null) {
//        try {
//          rs.close();
//        } catch (SQLException e) {}
//      }
//      if (ps != null) {
//        try {
//          ps.close();
//        } catch (SQLException e) {}
//      }
//      if (c != null) {
//        try {
//          c.close();
//        } catch (SQLException e) {}
//      }
//    }
//  }
}
