package org.mobilohas.bell.ch1.user.domain;

public class User {

  String id;
  String name;
  String password;

  public User() {}

  public User(final String id, final String name, final String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }
}
