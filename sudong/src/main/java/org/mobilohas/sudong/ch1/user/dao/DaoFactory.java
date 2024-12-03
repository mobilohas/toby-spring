package org.mobilohas.sudong.ch1.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }
    @Bean
    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }
    @Bean
    public ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }
}
