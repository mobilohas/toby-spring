package org.mobilohas.sudong.ch1.user;

import java.sql.SQLException;

import org.mobilohas.sudong.ch1.user.dao.DaoFactory;
import org.mobilohas.sudong.ch1.user.dao.UserDao;
import org.mobilohas.sudong.ch1.user.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao = ac.getBean("userDao", UserDao.class);

    User user = new User();
    user.setId("sudong5");
    user.setName("박지수");
    user.setPassword("1234qwer!!");

    dao.add(user);

    System.out.println(user.getId() + " 등록 성공");

    User user2 = dao.get(user.getId());
    System.out.println(user2.getName());
    System.out.println(user2.getPassword());
    System.out.println(user2.getId() + " 조회 성공");

    if (!user.getName().equals(user2.getName())) {
      System.out.println("테스트 실패 (name)");
    }
    else if(!user.getPassword().equals(user2.getPassword())) {
      System.out.println("테스트 실패(password)");
    }
    else{
      System.out.println("조회 테스트 성공");
    }
  }
}
