package org.mobilohas.bell.template;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

  public int calcSum(final String path) throws IOException {
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(path));
      Integer sum = 0;
      String line = null;
      while( (line = br.readLine()) != null ){
        sum += Integer.parseInt(line);
      }
      return sum;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw e;
    } finally {
      if (br != null) {
        br.close();
      }
    }
  }
}
