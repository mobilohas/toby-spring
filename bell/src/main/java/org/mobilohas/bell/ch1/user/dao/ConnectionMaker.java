package org.mobilohas.bell.ch1.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

  Connection makeConnection() throws ClassNotFoundException, SQLException;
}
