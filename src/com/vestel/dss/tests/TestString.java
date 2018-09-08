package com.vestel.dss.tests;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

/**
 * FileName: TestString
 * Author: Chris
 * Date: 2018/9/6 17:33
 * Description: Test String
 */
public class TestString {

    public static void main(String[] args) {
        System.out.println(testTrim("     adefa ewfs fwe     "));
        System.out.println(testTrim("adbfaed e   sd "));
        System.out.println(testTrim("ABCD   EFGH"));
        System.out.println();
        System.out.println(testFan("abcdefg"));
        System.out.println(testFan("abcdefg", 3, 5));
        String value = new String("value");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    }



    private static String testFan(String msg) {
        return testFan(msg, 0, msg.length());
    }

    private static String testFan(String msg, int start, int end) {
        String s = msg.substring(0, start);
        String e = msg.substring(end, msg.length());
        String z = "";
        for (int i = end - 1; i >= start; i--) {
            char c = msg.charAt(i);
            z += c;
        }
        return s + z + e;

    }

    @Test
    private static String testTrim(String msg) {
        boolean first = false;
        String s1 = "", s2 = "";
        int length = msg.length();
        for (int i = length - 1; i >= 0; i--) {
            char c = msg.charAt(i);
            if (c == ' ') {
                if (first) {
                    s1 += c;
                }
            } else {
                first = true;
                s1 += c;
            }
        }
        System.out.println("s1 = " + s1);
        int length1 = s1.length();
        first = false;
        for (int i = length1 - 1; i >= 0; i--) {
            char c = s1.charAt(i);
            if (c == ' ') {
                if (first) {
                    s2 += c;
                }
            } else {
                first = true;
                s2 += c;
            }
        }
        //return msg.trim();
        return s2;
    }
}
