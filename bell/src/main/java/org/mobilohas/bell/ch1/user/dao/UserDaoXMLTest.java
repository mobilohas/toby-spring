package org.mobilohas.bell.ch1.user.dao;

import java.sql.SQLException;
import org.mobilohas.bell.ch1.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoXMLTest {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    ApplicationContext context = new GenericXmlApplicationContext(
        "applicationContext.xml");
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
