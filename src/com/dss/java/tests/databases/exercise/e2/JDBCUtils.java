package com.dss.java.tests.databases.exercise.e2;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * FileName: JDBCUtils
 * Author: Chris
 * Date: 2018/11/2 10:15
 * Description: JDBC 工具类，用于操作数据库
 */
public class JDBCUtils {

    private static ComboPooledDataSource sDataSource;

    static {
        sDataSource = new ComboPooledDataSource("con_pool_mysql");
    }

    public static DataSource getDataSource() {
        return sDataSource;
    }

    /**
     * 获得数据库连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return sDataSource.getConnection();
    }

    /**
     * 关闭结果集和Statement
     *
     * @param set       结果集
     * @param statement
     * @throws SQLException
     */
    public static void closeResultSet(ResultSet set, Statement statement) throws SQLException {
        if (set != null) {
            set.close();
        }
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭数据库连接、Statement 和 结果集
     *
     * @param connection
     * @param statement
     * @param set
     * @throws SQLException
     */
    public static void closeDB(Connection connection, Statement statement, ResultSet set) throws SQLException {
        if (set != null) {
            set.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * 开始事务
     *
     * @param connection
     */
    public static void beginTransaction(Connection connection) throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);
        }
    }

    /**
     * 提交事务
     *
     * @param connection
     * @throws SQLException
     */
    public static void commitTransaction(Connection connection) throws SQLException {
        if (connection != null) {
            connection.commit();
        }
    }

    /**
     * 回滚事务
     *
     * @param connection
     * @throws SQLException
     */
    public static void rollbackTransaction(Connection connection) throws SQLException {
        if (connection != null) {
            connection.rollback();
        }
    }

}
