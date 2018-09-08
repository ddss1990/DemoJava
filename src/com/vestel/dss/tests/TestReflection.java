package com.vestel.dss.tests;

import org.junit.jupiter.api.Test;

/**
 * FileName: TestReflection
 * Author: Chris
 * Date: 2018/9/7 16:30
 * Description: Test Reflection
 */
public class TestReflection {

    public static void main(String[] args) {
        test1();
    }

    @Test
    private static void test1() {
        System.out.println("Hello world!");
        Object o = null;
        try {
            Class<?> aClass = Class.forName("com.vestel.dss.bean.Cat");
            o = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("o = " + o);
    }
}
