package com.dss.java.tests;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * FileName: TestBase
 * Author: Chris
 * Date: 2018/9/10 11:43
 * Description: Test Base
 */
public class TestBase {

    @Test
    public void test() {
        Date date = new Date(System.currentTimeMillis());
        String s = date.toString();

        InputStream is = getClass().getClassLoader().getResourceAsStream("test.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
            String user = properties.getProperty("user", "null");
            System.out.println("user = " + user);
            String name = properties.getProperty("name", "Tom");
            System.out.println("name = " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPotato() {
        Random random = new Random();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int potato = random.nextInt(101) + 200;
            //System.out.println("potato = " + potato);
            list.add(potato);
        }
        int[][] potatoSum = new int[8][8];
        for (int i = 0; i < potatoSum.length; i++) {
            for (int j = 0; j < potatoSum.length; j++) {
                int sum = list.get(i) + list.get(j);
                if (i == j) sum = 0;
                potatoSum[i][j] = sum - 500;
            }
        }
        for (int i = 0; i < potatoSum.length; i++) {
            System.out.println("potatoSum = " + Arrays.toString(potatoSum[i]));
        }
        for (int i = 0; i < potatoSum.length; i++) {
            for (int j = 0; j < potatoSum[i].length; j++) {
                int potatoDiff = potatoSum[i][j];
                
            }
        }


    }
}
