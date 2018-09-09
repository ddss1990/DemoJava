package com.dss.java.tests.internet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * User: DSS
 * Date: 2018/9/9
 * Time: 18:39
 * Tag: Test Internet
 */
public class TestInternet {
    public static void main(String[] args) {
        testIP();
    }

    private static void testIP() {
        try {
            InetAddress inetAddress = InetAddress.getByName("www.12306.cn");
            byte[] address = inetAddress.getAddress();
            String canonicalHostName = inetAddress.getCanonicalHostName();
            String hostAddress = inetAddress.getHostAddress(); // IP地址// 112.65.93.15
            String hostName = inetAddress.getHostName();// 域名
            System.out.println("inetAddress = " + inetAddress + ", canonicalHostName = " + canonicalHostName + ", hostAddress = " +
                    hostAddress + ", hostName = " + hostName + ", address" + Arrays.toString(address));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
