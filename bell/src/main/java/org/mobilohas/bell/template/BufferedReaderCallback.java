package org.mobilohas.bell.template;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderCallback {
  Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
