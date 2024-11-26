package org.mobilohas.green.ch1.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    private DConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
