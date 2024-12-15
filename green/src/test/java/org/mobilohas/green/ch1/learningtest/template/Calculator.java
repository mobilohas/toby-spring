package org.mobilohas.green.ch1.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filePath) throws IOException {
        LineCallBack<Integer> sumCallBack = (line, value) -> value + Integer.valueOf(line);
        return lineReadTemplate(filePath, sumCallBack, 0);
    }


    public Integer calcMultiply(String filePath) throws IOException {
        LineCallBack<Integer> multiplyCallback = (line, value) -> value * Integer.valueOf(line);
        return lineReadTemplate(filePath, multiplyCallback, 1);
    }

    public String concatenate(String filePath) throws IOException {
        LineCallBack<String> concatenateCallBack = (line, value) -> value + line;
        return lineReadTemplate(filePath, concatenateCallBack, "");
    }

    public <T> T lineReadTemplate(String filePath, LineCallBack<T> callback, T intVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            T res = intVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                res = callback.doSomethineWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
