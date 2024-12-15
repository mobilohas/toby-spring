package org.mobilohas.bell.ch3.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public class JdbcContext {
  private DataSource dataSource;

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
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

  public void executeSql(final String query) throws SQLException {
    workWithStatementStrategy(c -> c.prepareStatement(query));
  }

  public void executeSql(final String query, final Object... params) throws SQLException {
    workWithStatementStrategy(c -> {
      PreparedStatement ps = c.prepareStatement(query);
      for (int i = 0; i < params.length; i++) {
        final Object param = params[i];
        setParameter(ps, i, param);
      }
      return ps;
    });
  }

  private void setParameter(final PreparedStatement ps, final int index, final Object param) throws SQLException {
    if (param instanceof String) {
      ps.setString(index+1, (String) param);
    } else if (param instanceof Integer) {
      ps.setInt(index+1, (Integer) param);
    }
  }
}
