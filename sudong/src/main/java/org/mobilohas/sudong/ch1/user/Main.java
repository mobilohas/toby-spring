package org.mobilohas.sudong.ch1.user;

import java.sql.SQLException;

import org.mobilohas.sudong.ch1.user.dao.NConnectionMaker;
import org.mobilohas.sudong.ch1.user.dao.UserDao;
import org.mobilohas.sudong.ch1.user.domain.User;

public class Main {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    UserDao dao = new UserDao(new NConnectionMaker());

    User user = new User();
    user.setId("sudong4");
    user.setName("박지수");
    user.setPassword("1234qwer!!");

    dao.add(user);

    System.out.println(user.getId() + " 등록 성공");

    User user2 = dao.get(user.getId());
    System.out.println(user2.getName());
    System.out.println(user2.getPassword());
    System.out.println(user2.getId() + " 조회 성공");
  }
}
