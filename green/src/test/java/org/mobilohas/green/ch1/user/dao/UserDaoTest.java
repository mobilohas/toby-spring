package org.mobilohas.green.ch1.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mobilohas.green.ch1.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
@DirtiesContext
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao dao;

    @Before
    public void setUp() {
        this.dao = this.context.getBean("userDao", UserDao.class);
        System.out.println(this.context);
        System.out.println(this);

        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3307/spring", "root", "qwer1234", true
        );
        dao.setDataSource(dataSource);
    }

    @Test
    public void addAndGet() throws SQLException {

        User user = new User("green", "홍길동", "hong1234");
        User user2 = new User("green2", "이순신", "lee1234");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user.getId());
        assertThat(userget1.getName(), is(user.getName()));
        assertThat(userget1.getPassword(), is(user.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void userUserFailure() throws SQLException {
        dao.deleteAll();

        assertThat(dao.getCount(), is(0));

        dao.get("unknown");
    }

    @Test
    public void getCount() throws SQLException {

        User user = new User("green", "홍길동", "hong1234");
        User user2 = new User("green2", "이순신", "lee1234");
        User user3 = new User("green3", "심청이", "sim1234");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }
}
