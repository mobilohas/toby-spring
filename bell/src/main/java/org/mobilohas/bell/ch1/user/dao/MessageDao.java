package org.mobilohas.bell.ch1.user.dao;

public class MessageDao {

  private final ConnectionMaker connectionMaker;

  public MessageDao(final ConnectionMaker connectionMaker) {
    this.connectionMaker = connectionMaker;
  }
}
