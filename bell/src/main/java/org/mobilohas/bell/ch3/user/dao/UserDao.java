package org.mobilohas.bell.ch3.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.mobilohas.bell.ch3.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao {

  private DataSource dataSource;

  public UserDao() {}

  public UserDao(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void add(User user) throws SQLException {
    jdbcContextWithStatementStrategy(c -> {
      PreparedStatement ps = c.prepareStatement(
          "insert into users(id, name, password) values(?,?,?)");
      ps.setString(1, user.getId());
      ps.setString(2, user.getName());
      ps.setString(3, user.getPassword());
      return ps;
    });
  }

  public User get(String id) throws SQLException {
    Connection c = dataSource.getConnection();

    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?");
    ps.setString(1, id);

    ResultSet rs = ps.executeQuery();

    User user = null;
    if (rs.next()) {
      user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
    }

    rs.close();
    ps.close();
    c.close();

    if (user == null) {
      throw new EmptyResultDataAccessException(1);
    }

    return user;
  }

  public int getCount() throws SQLException {
    Connection c = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
      c = dataSource.getConnection();
      ps = c.prepareStatement("select count(*) from users");
      rs = ps.executeQuery();
      rs.next();
      return rs.getInt(1);
    } catch (SQLException e) {
      throw e;
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {}
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {}
      }
      if (c != null) {
        try {
          c.close();
        } catch (SQLException e) {}
      }
    }
  }

  public void deleteAll() throws SQLException {
    jdbcContextWithStatementStrategy(c -> c.prepareStatement("delete from users"));
  }

  private void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
    Connection c = null;
    PreparedStatement ps = null;
    try {
      c = dataSource.getConnection();

      ps = strategy.makePreparedStatement(c);

      ps.executeUpdate();
    } catch (SQLException e) {
      throw e;
    } finally {
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {}
      }
      if (c != null) {
        try {
          c.close();
        } catch (SQLException e) {}
      }
    }
  }

  // XML 설정을 위한 Setter
  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }
}
