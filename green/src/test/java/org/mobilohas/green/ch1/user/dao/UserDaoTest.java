package org.mobilohas.green.ch1.user.dao;

import org.mobilohas.green.ch1.user.domain.User;

import java.sql.DriverManager;
import java.sql.SQLException;


class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao dao = new UserDao(connectionMaker);

        User user = new User();
        user.setId("green3");
        user.setName("홍길동");
        user.setPassword("hong1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}
