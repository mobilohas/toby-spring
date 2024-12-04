package org.mobilohas.bell.ch1.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import org.junit.Test;
import org.mobilohas.bell.ch1.user.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoTest  {

  @Test
  public void addAndGet() throws SQLException, ClassNotFoundException {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    UserDao dao = context.getBean("userDao", UserDao.class);

    User user1 = new User("bell", "홍길동", "hong1234");
    User user2 = new User("lee", "이몽룡", "lee1234");
    User user3 = new User("kim", "김삿갓", "kim1234");

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    assertThat(dao.getCount(), is(1));

    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    dao.add(user3);
    assertThat(dao.getCount(), is(3));

    User result = dao.get(user1.getId());
    assertThat(result.getName(), is(user1.getName()));
    assertThat(result.getPassword(), is(user1.getPassword()));
  }
}