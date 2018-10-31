package com.dss.java.tests.databases.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

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

    /**
     * 获得数据库连接对象
     */
    @Deprecated
    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = getConnection("jdbc.properties");
        return connection;
    }

    /**
     *  声明一个静态的数据库连接池对象，并在静态代码块中通过加载配置文件初始化
     */
    private static ComboPooledDataSource sDataSource;
    static {
        sDataSource = new ComboPooledDataSource("con_pool_chris");
    }

    /**
     * 通过数据库连接池去获得数据库连接对象，
     * 通过这种方式得到的数据库连接对象，释放资源时并不是真的关闭连接，而是将数据库连接对象归还给数据库连接池
     * @return
     * @throws SQLException
     */
    public static Connection getConnection2() throws SQLException {
        return sDataSource.getConnection();
    }

    /**
     * 通过读取配置文件，获取数据库连接
     *
     * @param name
     * @return
     */
    @Deprecated
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

    /**
     * 关闭自动提交，开启事务
     * @param connection
     */
    public static void beginTransaction(Connection connection) {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     * @param connection
     */
    public static void commitTransaction(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     * @param connection
     */
    public static void rollbackTransaction(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
