package com.dss.java.tests.internet;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * FileName: TestURL
 * Author: Chris
 * Date: 2018/9/12 17:37
 * Description: Test URL
 */
public class TestURL {
    @Test
    public void test() {
        try {
            URL url = new URL("https://mp.weixin.qq.com/s/iyz9MTqdPvdHjtVy42SPhA");
            //getUrlString(url);
//            HttpURLConnection connection = new HttpURLConnection(url);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = connection.getOutputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getUrlString(URL url) {
        String protocol = url.getProtocol(); // 协议名
        String host = url.getHost(); // 主机名
        int port = url.getPort(); // 端口号
        String path = url.getPath(); // 文件路径
        String file = url.getFile(); // 文件名
        String ref = url.getRef(); // 在文件中的相对位置
        String query = url.getQuery(); // 查询名，一般是指URL中'?'后的内容
        System.out.println("protocol = " + protocol);
        System.out.println("host = " + host);
        System.out.println("port = " + port);
        System.out.println("path = " + path);
        System.out.println("file = " + file);
        System.out.println("ref = " + ref);
        System.out.println("query = " + query);
    }
}
