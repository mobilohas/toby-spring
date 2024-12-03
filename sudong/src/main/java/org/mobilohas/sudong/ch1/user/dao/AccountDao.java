package org.mobilohas.sudong.ch1.user.dao;

public class AccountDao {
    private ConnectionMaker connectionMaker;
    public AccountDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
