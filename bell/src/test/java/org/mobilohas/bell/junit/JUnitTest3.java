package org.mobilohas.bell.junit;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/junit.xml")
public class JUnitTest3 {

  @Autowired
  ApplicationContext applicationContext;

  static Set<JUnitTest3> testObjects = new HashSet<>();
  static ApplicationContext contextObject = null;

  @Test
  public void test1() {
    assertThat(testObjects, not(hasItems(this)));
    testObjects.add(this);
    assertThat(contextObject == null || contextObject == this.applicationContext, is(true));
    contextObject = this.applicationContext;
  }

  @Test
  public void test2() {
    assertThat(testObjects, not(hasItems(this)));
    testObjects.add(this);
    assertThat(contextObject == null || contextObject == this.applicationContext, is(true));
    contextObject = this.applicationContext;
  }

  @Test
  public void test3() {
    assertThat(testObjects, not(hasItems(this)));
    testObjects.add(this);
    assertThat(contextObject == null || contextObject == this.applicationContext, is(true));
    contextObject = this.applicationContext;
  }
}
