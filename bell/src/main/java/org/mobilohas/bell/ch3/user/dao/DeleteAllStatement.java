package org.mobilohas.bell.ch3.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {
  @Override
  public PreparedStatement makePreparedStatement(final Connection c) throws SQLException {
    return c.prepareStatement("delete from users");
  }
}
