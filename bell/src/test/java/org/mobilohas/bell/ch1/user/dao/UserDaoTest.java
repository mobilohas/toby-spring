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

    User user = new User();
    user.setId("bell");
    user.setName("홍길동");
    user.setPassword("hong1234");

    dao.add(user);

    User user2 = dao.get(user.getId());
    assertThat(user2.getName(), is(user.getName()));
    assertThat(user2.getPassword(), is(user.getPassword()));
  }
}