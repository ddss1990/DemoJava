package com.dss.java.tests.databases.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * FileName: JDBCUtils
 * Author: Chris
 * Date: 2018/10/23 17:41
 * Description: JDBC utils
 */
public class JDBCUtils {

    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = getConnection("jdbc.properties");
        return connection;
    }

    /**
     * 通过读取配置文件，获取数据库连接
     *
     * @param name
     * @return
     */
    public static Connection getConnection(String name) throws IOException, ClassNotFoundException, SQLException {
        // 1. 获取输入流
        InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream(name);
        Properties properties = new Properties();
        // 2. 加载输入流
        properties.load(is);
        // 3. 获得连接数据库需要的字段
        String driverClass = properties.getProperty("driver");
        String jdbcUrl = properties.getProperty("jdbcUrl");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        // 4. 加载驱动
        Class.forName(driverClass);
        // 5. 获得连接
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        return connection;
    }

    /**
     * 关闭连接，释放资源
     *
     * @param statement
     * @param connection
     */
    public static void releaseConnection(Statement statement, Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接，释放资源
     *
     * @param statement
     * @param connection
     */
    public static void releaseConnection(ResultSet set, Statement statement, Connection connection) {
        if (set != null) {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
