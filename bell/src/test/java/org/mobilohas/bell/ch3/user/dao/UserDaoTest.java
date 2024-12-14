package org.mobilohas.bell.ch3.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mobilohas.bell.ch3.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext-ch3.xml")
public class UserDaoTest  {

  @Autowired
  private UserDao dao;

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
  public void getUserFailure() throws SQLException {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.get("unknown_id");
  }

  @Test
  public void getAll() throws SQLException {
    dao.deleteAll();

    User user1 = new User("bell", "홍길동", "hong1234");
    User user2 = new User("lee", "이몽룡", "lee1234");
    User user3 = new User("kim", "김삿갓", "kim1234");

    dao.add(user1);
    List<User> users1 = dao.getAll();
    assertThat(users1.size(), is(1));
    checkSameUser(user1, users1.get(0));

    dao.add(user2);
    List<User> users2 = dao.getAll();
    assertThat(users2.size(), is(2));
    checkSameUser(user1, users2.get(0));
    checkSameUser(user2, users2.get(1));

    dao.add(user3);
    List<User> users3 = dao.getAll();
    assertThat(users3.size(), is(3));
    checkSameUser(user1, users3.get(0));
    checkSameUser(user3, users3.get(1));
    checkSameUser(user2, users3.get(2));
  }

  private void checkSameUser(final User user1, final User user2) {
    assertThat(user1.getId(), is(user2.getId()));
    assertThat(user1.getName(), is(user2.getName()));
    assertThat(user1.getPassword(), is(user2.getPassword()));
  }
}