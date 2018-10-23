package com.dss.java.tests;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * FileName: TestBase
 * Author: Chris
 * Date: 2018/9/10 11:43
 * Description: Test Base
 */
public class TestBase {

    @Test
    public void test(){
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
}
