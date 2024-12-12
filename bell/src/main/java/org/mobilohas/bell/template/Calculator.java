package org.mobilohas.bell.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

  public Integer fileReadTemplates(final String path, BufferedReaderCallback callback) throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path));
      return callback.doSomethingWithReader(br);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw e;
    } finally {
      if (br != null) {
        br.close();
      }
    }
  }

  public Integer calcSum_fileReadTemplates(final String path) throws IOException {
    return fileReadTemplates(path, br -> {
      Integer sum = 0;
      String line = null;
      while( (line = br.readLine()) != null ){
        sum += Integer.parseInt(line);
      }
      return sum;
    });
  }

  public Integer calcMultiply_fileReadTemplates(final String path) throws IOException {
    return fileReadTemplates(path, br -> {
      Integer result = 1;
      String line = null;
      while( (line = br.readLine()) != null ){
        result *= Integer.parseInt(line);
      }
      return result;
    });
  }

  public Integer lineReadTemplates(final String path, int initVal, LineCallback callback) throws IOException {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path));
      Integer res = initVal;
      String line = null;
      while( (line = br.readLine()) != null ){
        res = callback.doSomethingWithLine(line, res);
      }
      return res;
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw e;
    } finally {
      if (br != null) {
        br.close();
      }
    }
  }

  public Integer calcSum(final String path) throws IOException {
    return lineReadTemplates(path, 0 , (line, res) -> res + Integer.parseInt(line));
  }

  public Integer calcMultiply(final String path) throws IOException {
    return lineReadTemplates(path, 1 , (line, res) -> res * Integer.parseInt(line));
  }
}
