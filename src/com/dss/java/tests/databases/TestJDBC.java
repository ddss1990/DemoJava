package com.dss.java.tests.databases;

import com.dss.java.tests.databases.utils.JDBCUtils;
import com.mysql.jdbc.Driver;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
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
            //java.sql.Driver driver2 = new java.sql.Driver();
            // 1. 创建一个Driver类实现对象
            Driver driver = new Driver();
            com.mysql.cj.jdbc.Driver driver1 = new com.mysql.cj.jdbc.Driver();
            // 2. 连接数据库的基本信息
            String url = "jdbc:mysql://localhost:3306/myemployees" + "?serverTimezone=GMT%2B8";
            Properties info = new Properties();
            info.put("user", "root");
            info.put("password", "chris");
            // 3. 连接数据库
            Connection connect = driver1.connect(url, info);
            System.out.println("connect = " + connect);


            //Connection connection = DriverManager.getConnection(url);
            //DriverManager.registerDriver(driver);
            //System.out.println("connection = " + connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将获得数据库连接封装起来，只需要提供文件目录即可，将连接所用的信息都放到文件中
     *
     * @param name 文件名
     * @return 数据库连接对象
     * @throws Exception
     */
    public Connection getConnection(String name) throws Exception {
        // 将连接数据库所用到的信息都存入到该文件中，通过ClassLoader得到输入流
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name);
        Properties properties = new Properties();
        properties.load(inputStream);
        // 从Properties读取指定的数据
        String driverClass = properties.getProperty("driver");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        // 封装连接数据库所用到的信息
        Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);

        // 通过反射去获得Driver对象，然后再去执行connect方法连接数据库并获得连接对象
        Class<?> aClass = Class.forName(driverClass);
        Object o = aClass.newInstance();
        Method method_connect = aClass.getMethod("connect", String.class, Properties.class);
        Connection connection = (Connection) method_connect.invoke(o, jdbcUrl, info);

        // 这里也可以直接使用Driver类，这里的Driver类是java.sql. 下的，是所有各个数据库厂商定义的Driver的父类
        java.sql.Driver driver = (java.sql.Driver) Class.forName(driverClass).newInstance();
        Connection connect = driver.connect(jdbcUrl, info);
        return connection;
    }

    @Test
    public void testConnection() {
        try {
            Connection connection = getConnection("jdbc.properties");
            System.out.println("connection = " + connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过DriverManager获取数据库连接对象
     */
    private Connection getConnection() throws Exception {
        // 1. 准备连接数据库的信息
        String jdbcUrl = null;
        String driverClass = null;
        String user = null;
        String password = null;

        // 将连接数据库所用到的信息都存入到该文件中，通过ClassLoader得到输入流
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        // 从Properties读取指定的数据
        driverClass = properties.getProperty("driver");
        jdbcUrl = properties.getProperty("jdbcUrl");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

        // 2. 加载驱动程序
        Class.forName(driverClass);

        // 3 通过DriverManager获得数据库连接对象
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        return connection;
    }

    @Test
    public void testDriverManager() {
        Connection connection = null;
        try {
            connection = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("connection = " + connection);
    }

    /**
     * 通过JDBC操作数据表
     */
    @Test
    public void testUpdate() throws Exception {
        // 1. 获取数据库连接
        Connection connection = getConnection();
        // 2. 准备SQL语句
        String sql = "insert into test_jdbc_users(name, email, brith) " +
                "values('Tom', 'tom@abc.com', curdate())";
        // 3. 获取Statement对象，用于执行SQL语句
        Statement statement = connection.createStatement();
        // 4. 执行SQL语句
        //statement.execute(sql);
        statement.executeUpdate(sql);
        // 5. 关闭Statement
        statement.close();
        // 6. 关闭连接
        connection.close();
    }

    /**
     * 通过ResultSet执行查询操作
     */
    @Test
    public void testQuery() throws Exception {
        // 1. 获取数据库连接
        Connection connection = getConnection();
        // 2. 获得Statement对象
        Statement statement = connection.createStatement();
        // 3. 准备SQL语句
        String sql = "select _id, name, email, brith from test_jdbc_users";
        // 4. 执行SQL语句，获得ResultSet结果集
        ResultSet set = statement.executeQuery(sql);
        // 5. 处理结果集
        while (set.next()) {
            int id = set.getInt("_id");
            String name = set.getString(2);
            Date birth = set.getDate("brith");
            String email = set.getString("email");
            System.out.println("id = " + id + ", name = " + name + ", email = " + email + ", birth = " + birth);
        }
        // 6. 释放资源
        JDBCUtils.releaseConnection(set, statement, connection);
    }
}
