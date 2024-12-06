package org.mobilohas.bell.ch1.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mobilohas.bell.ch1.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class UserDaoTest2 {

  private UserDao dao;

  @Before
  public void setUp() {
    dao =new UserDao();
    DataSource dataSource = new SingleConnectionDataSource(
        "jdbc:mysql://localhost:3307/testdb", "root", "qwer1234", true);
    dao.setDataSource(dataSource);
  }

  @Test
  public void addAndGet() throws SQLException, ClassNotFoundException {
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

    User userget1 = dao.get(user1.getId());
    assertThat(userget1.getName(), is(user1.getName()));
    assertThat(userget1.getPassword(), is(user1.getPassword()));

    User userget2 = dao.get(user2.getId());
    assertThat(userget2.getName(), is(user2.getName()));
    assertThat(userget2.getPassword(), is(user2.getPassword()));

    User userget3 = dao.get(user3.getId());
    assertThat(userget3.getName(), is(user3.getName()));
    assertThat(userget3.getPassword(), is(user3.getPassword()));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void getUserFailure() throws SQLException, ClassNotFoundException {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    UserDao dao = context.getBean("userDao", UserDao.class);
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.get("unknown_id");
  }
}