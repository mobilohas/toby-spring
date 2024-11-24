package org.mobilohas.bell.ch1.user.dao;

import java.sql.SQLException;
import org.mobilohas.bell.ch1.user.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao = context.getBean("userDao", UserDao.class);

    User user = new User();
    user.setId("bell");
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
