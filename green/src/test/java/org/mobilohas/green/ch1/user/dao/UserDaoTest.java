package org.mobilohas.green.ch1.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mobilohas.green.ch1.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
@DirtiesContext
public class UserDaoTest {

    private UserDao dao;

    @Before
    public void setUp() {
        this.dao = new UserDao();

        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost:3307/testdb", "root", "qwer1234", true
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

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();

        assertThat(dao.getAll().size(), is(0));

        User user1 = new User("green", "홍길동", "hong1234");
        User user2 = new User("green2", "이순신", "lee1234");
        User user3 = new User("green3", "심청이", "sim1234");
        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSamUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSamUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSamUser(user3, users3.get(2));
    }

    private void checkSamUser(User user1, User user2) {
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getId(), is(user2.getId()));

    }
}
