package org.mobilohas.green.ch1.user.dao;

import org.mobilohas.green.ch1.user.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnctionMaker());
    }

    @Bean
    public ConnectionMaker realConnctionMaker() {
        return new DConnectionMaker();
    }

}

