package org.mobilohas.bell.ch1.user.dao;

public class AccountDao {

  private final ConnectionMaker connectionMaker;

  public AccountDao(final ConnectionMaker connectionMaker) {
    this.connectionMaker = connectionMaker;
  }
}
