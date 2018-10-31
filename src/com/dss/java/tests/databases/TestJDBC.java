package com.dss.java.tests.databases;

import com.dss.java.tests.databases.utils.DAO;
import com.dss.java.tests.databases.utils.JDBCUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Driver;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.*;
import java.lang.reflect.Method;
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
        String sql = "insert into test_jdbc_users(name, email, birth) " +
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

    /**
     * 测试PrepareStatement
     */
    @Test
    public void testPrepareStatement() {
        try {
            insertPrepareStatement("张三", "zhang3@abc.com", System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用PrepareStatement插入数据
     *
     * @param name
     * @param email
     * @param birth
     */
    private void insertPrepareStatement(String name, String email, long birth) throws Exception {
        // 1. 获得数据库连接
        Connection connection = getConnection();
        // 2. 准备PrepareStatement用的SQL语句
        String sql = "insert into test_jdbc_users(name, email, brith) values(?,?,?);";
        // 3. 获得PrepareStatement对象
        PreparedStatement statement = connection.prepareStatement(sql);
        // 4. 为SQL语句的占位符设置值
        statement.setString(1, name);
        statement.setString(2, email);
        statement.setDate(3, new Date(birth));
        // 5. 执行
        statement.executeUpdate();
        // 6. 释放连接
        JDBCUtils.releaseConnection(statement, connection);
    }


    /**
     * 使用PrepareStatement插入数据，升级版
     *
     * @param sql
     * @param args
     * @throws Exception
     */
    private void insertPrepareStatement(String sql, Object... args) throws Exception {
        // 1. 获得数据库连接
        Connection connection = getConnection();
        // 2. 获得PrepareStatement对象
        PreparedStatement statement = connection.prepareStatement(sql);
        // 3. 为SQL语句的占位符设置值
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            statement.setObject(i + 1, arg);
        }
        // 4. 执行
        statement.executeUpdate();
        // 5. 释放连接
        JDBCUtils.releaseConnection(statement, connection);
    }

    /**
     * 测试填充大数据类型
     */
    @Test
    public void testSetBlob() {
        setBlob();
    }

    /**
     * 插入大对象类型的数据
     */
    private void setBlob() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into test_jdbc_users(name, email, birth, picture) values(?,?,?,?)";
            statement = connection.prepareStatement(sql);
            // 替换占位符
            statement.setString(1, "张飞");
            statement.setString(2, "zhangfei@abc.com");
            statement.setDate(3, new Date(System.currentTimeMillis()));
            InputStream blob = new FileInputStream("C:\\Users\\Chris\\Pictures\\飞机\\F-35B.jpg");
            statement.setBlob(4, blob);
            // 执行SQL
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseConnection(statement, connection);
        }
    }

    /**
     * 测试读取大对象类型的数据
     */
    @Test
    public void testGetBlob() {
        getBlob();
    }

    /**
     * 读取大对象类型的数据
     */
    private void getBlob() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select _id, name , email, birth, picture from test_jdbc_users where _id = ?";
            statement = connection.prepareStatement(sql);
            // 替换占位符
            int id = 1006;
            statement.setInt(1, id);
            // 得到结果集
            resultSet = statement.executeQuery();
            // 解析结果集
            if (resultSet.next()) {
                int _id = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");
                System.out.println("_id = " + _id + ", name = " + name + ", email = " + email + ", birth = " + birth);
                // 得到大对象
                Blob blob = resultSet.getBlob(5);
                // 通过大对象得到输入流
                InputStream is = blob.getBinaryStream();
                OutputStream os = new FileOutputStream("picture.jpg");
                // 将读取出来的大对象文件保存下来
                int len = 0;
                byte[] datas = new byte[1024];
                while ((len = is.read(datas)) != -1) {
                    os.write(datas, 0, len);
                }
                os.close();
                is.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseConnection(resultSet, statement, connection);
        }
    }

    /**
     * 测试事务，用户1向用户2转账500
     */
    @Test
    public void testTransaction() {
        Connection connection = null;
        try {
            DAO dao = new DAO();
            // 使用事务，需要共用一个Connection
            connection = JDBCUtils.getConnection();
            boolean autoCommit = connection.getAutoCommit();
            System.out.println("autoCommit = " + autoCommit);
            // 并且需要关闭自动提交
            connection.setAutoCommit(false);

            // 用户1的余额减去500
            String sql = "update users set balance = balance - 500 where _id = 1";
            dao.update(connection, sql);

            // 人为的加上一个错误，查看结果，当不使用事务的时候，会看到用户1的余额有变动，2的无变动
            // 保证事务的唯一性，不能出现一个用户的余额变动，另外一个用户的余额不变
            int i = 10 / 0;

            // 用户2的余额加上500
            sql = "update users set balance = balance + 500 where _id = 2";
            dao.update(connection, sql);

            // 没有错误，就对事务进行提交
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // 出现异常，就进行回滚事务
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCUtils.releaseConnection(null, connection);
        }
    }

    /**
     * 测试批处理操作
     */
    @Test
    public void testBatch() {
        long s = batchWithStatement(); // 13737
        System.out.println("用时 " + s + " ms");
        long ps = batchWithPreparedStatement(); // 12560
        System.out.println("用时 " + ps + " ms");
    }

    /**
     * 使用PreparedStatement执行批处理操作
     *
     * @return
     */
    private long batchWithPreparedStatement() {
        Connection connection = null;
        PreparedStatement statement = null;
        long diff = 0;
        try {
            connection = JDBCUtils.getConnection();
            String baseSQL = "insert into big_data values(?, ?, ?);";
            statement = connection.prepareStatement(baseSQL);
            // 开启事务
            JDBCUtils.beginTransaction(connection);
            long begin = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                int start = 100001 + i;
                statement.setInt(1, start);
                statement.setString(2, "name_" + start);
                statement.setDate(3, new Date(System.currentTimeMillis()));
                statement.executeUpdate();
            }
            // 提交事务
            JDBCUtils.commitTransaction(connection);
            long end = System.currentTimeMillis();
            diff = end - begin;
        } catch (Exception e) {
            JDBCUtils.rollbackTransaction(connection);
            e.printStackTrace();
        }
        return diff;
    }

    /**
     * 使用Statement进行批量处理操作
     */
    private long batchWithStatement() {
        Connection connection = null;
        Statement statement = null;
        long diff = 0;

        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            // 开始事务
            JDBCUtils.beginTransaction(connection);
            // 配置SQL语句
            String baseSql = "insert into big_data values(%d, '%s', '%s');";
            long begin = System.currentTimeMillis();
            //System.out.println("sql = " + sql);
            for (int i = 0; i < 100000; i++) {
                String sql = String.format(baseSql, i + 1, "name_" + i, new Date(System.currentTimeMillis()));
                statement.executeUpdate(sql);
            }
            // 提交事务
            JDBCUtils.commitTransaction(connection);
            // 结束时间
            long end = System.currentTimeMillis();
            diff = end - begin;
        } catch (Exception e) {
            // 出现异常，回滚事务
            JDBCUtils.rollbackTransaction(connection);
            e.printStackTrace();
        }
        return diff;
    }

    /**
     * 测试数据库连接池
     */
    @Test
    public void testConnectionPool() {
        try {
            // 使用DBCP连接池来获取数据库连接对象
//            connectionPoolDBCP();
            // 通过DBCP工厂来获取数据库连接对象
//             dbcpFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用DBCPFactory来获得数据库连接对象
     */
    private void dbcpFactory() throws Exception {
        Properties properties = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("dbcp.properties");
        properties.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(properties);
        Connection connection = source.getConnection();
        System.out.println("connection = " + connection);
        printDBCPConfig((BasicDataSource) source);
    }

    /**
     * 使用DBCP来管理数据库连接池
     */
    private void connectionPoolDBCP() throws SQLException {
        // 创建 DataSource对象
        BasicDataSource source = new BasicDataSource();
        // 设置连接数据库需要的属性
        source.setUsername("root");
        source.setPassword("chris");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");

        printDBCPConfig(source);
        // 配置一些常用的属性 source.setMinIdle();
        source.setInitialSize(5);
        source.setMaxActive(5);
        source.setMinIdle(1);
        source.setMaxWait(3000);
        printDBCPConfig(source);

        // 通过DataSource获取数据库连接对象
        Connection connection = source.getConnection();
        System.out.println("connection = " + connection.getClass());

    }

    /**
     * 打印一些DBCP线程池的属性
     *
     * @param dataSource
     */
    private void printDBCPConfig(DataSource dataSource) throws SQLException {
        Connection connection = null;
        if (dataSource instanceof BasicDataSource) {
            BasicDataSource source = (BasicDataSource) dataSource;
            int initialSize = source.getInitialSize();
            int maxActive = source.getMaxActive();
            int maxIdle = source.getMaxIdle();
            int minIdle = source.getMinIdle();
            long maxWait = source.getMaxWait();
            System.out.println("initialSize = " + initialSize + ", maxActive = " + maxActive +
                    ", maxIdle = " + maxIdle + ", minIdle = " + minIdle + " maxWait = " + maxWait);
            connection = source.getConnection();
        } else if (dataSource instanceof ComboPooledDataSource) {
            ComboPooledDataSource source = (ComboPooledDataSource) dataSource;
            int initialPoolSize = source.getInitialPoolSize();
            int maxPoolSize = source.getMaxPoolSize();
            int minPoolSize = source.getMinPoolSize();
            int maxIdleTime = source.getMaxIdleTime();
            int acquireIncrement = source.getAcquireIncrement();
            System.out.println("initialPoolSize = " + initialPoolSize + " , maxPoolSize = " + maxPoolSize +
                    " , minPoolSize =" + minPoolSize + ", maxIdleTime = " + maxIdleTime + ", acquireIncrement = " + acquireIncrement);
            connection = source.getConnection();
        }
        System.out.println("connection = " + connection);
    }
}
