package org.mobilohas.bell.ch1.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.mobilohas.bell.ch1.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao {

  private DataSource dataSource;

  public UserDao() {}

  public UserDao(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c = dataSource.getConnection();

    PreparedStatement ps = c.prepareStatement(
        "insert into users(id, name, password) values(?,?,?)");
    ps.setString(1, user.getId());
    ps.setString(2, user.getName());
    ps.setString(3, user.getPassword());

    ps.executeUpdate();

    ps.close();
    c.close();
  }

  public User get(String id) throws ClassNotFoundException, SQLException {
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

  public int getCount() throws ClassNotFoundException, SQLException {
    Connection c = dataSource.getConnection();

    PreparedStatement ps = c.prepareStatement("select count(*) from users");

    ResultSet rs = ps.executeQuery();
    rs.next();
    int count = rs.getInt(1);

    rs.close();
    ps.close();
    c.close();
    return count;
  }

  public void deleteAll() throws ClassNotFoundException, SQLException {
    Connection c = dataSource.getConnection();
    PreparedStatement ps = c.prepareStatement("delete from users");
    ps.executeUpdate();

    ps.close();
    c.close();
  }

  // XML 설정을 위한 Setter
  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }
}
