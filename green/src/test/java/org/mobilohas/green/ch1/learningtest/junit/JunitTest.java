package org.mobilohas.green.ch1.learningtest.junit;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class JunitTest {
    static JunitTest testObject;
    static Set<JunitTest> testObjects = new HashSet<>();

    @Test
    public void test1() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test2() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test3() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test11() {
        assertThat(testObjects, not(hasItem(this)));
        testObject = this;
    }

    @Test
    public void test22() {
        assertThat(testObjects, not(hasItem(this)));
        testObject = this;
    }

    @Test
    public void test33() {
        assertThat(testObjects, not(hasItem(this)));
        testObject = this;
    }
}
