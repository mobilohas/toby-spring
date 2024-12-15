package org.mobilohas.bell.template;

public interface LineCallback<T> {
  T doSomethingWithLine(String line, T value);
}
