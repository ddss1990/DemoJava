package com.dss.java.tests;

import org.junit.Test;

import java.util.Date;

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
        
    }
}
