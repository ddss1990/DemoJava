package com.dss.java.tests.databases;

import com.mysql.jdbc.Driver;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * FileName: TestJDBC
 * Author: Chris
 * Date: 2018/10/22 15:43
 * Description: Test JDBC
 */
public class TestJDBC {
    @Test
    /**
     * Driver是一个接口：数据库厂商必须提供实现的接口，能从其中获取数据库连接
     * mysql-connector
     *
     */
    public void testDriver() {

        try {
            Driver driver = new Driver();
            com.mysql.cj.jdbc.Driver driver1 = new com.mysql.cj.jdbc.Driver();
            String url = "jdbc:mysql://localhost:3306/myemployees" + "?serverTimezone=GMT%2B8";
            Properties info = new Properties();
            info.put("user", "root");
            info.put("password", "chris");
            Connection connect = driver1.connect(url, info);
            System.out.println("connect = " + connect);


            //Connection connection = DriverManager.getConnection(url);
            //DriverManager.registerDriver(driver);
            //System.out.println("connection = " + connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
