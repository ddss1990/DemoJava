package com.dss.java.tests.databases.utils;

/**
 * FileName: DAO
 * Author: Chris
 * Date: 2018/10/29 11:46
 * Description: Data Access Object
 */

import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 访问信息数据的类
 */
public class DAO {

    /**
     * 查询得到一个具体的对象
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getObject(Class<T> clazz, String sql, Object... args) {
        List<T> beans = getObjectLists(clazz, sql, args);
        if (beans.size() > 0)
            return beans.get(0);
        return null;
    }

    /**
     * 通过结果集获得查询结果的列名
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private List<String> getColumnLabels(ResultSet resultSet) throws SQLException {
        List<String> labels = null;
        labels = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        for (int i = 0; i < count; i++) {
            String label = metaData.getColumnLabel(i + 1);
            labels.add(label);
        }
        return labels;
    }

    /**
     * 替换掉Statement中的占位符
     *
     * @param statement
     * @param sql
     * @param args
     * @throws SQLException
     */
    private void replacePlaceHolder(PreparedStatement statement, String sql, Object[] args) throws SQLException {
/*        PreparedStatement preparedStatement = null;
        if (statement instanceof PreparedStatement) {
            preparedStatement = (PreparedStatement) statement;
        } else {
            throw new IllegalArgumentException("The statement [" + statement + "] is not PreparedStatement");
        }*/
        if (args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }
    }

    /**
     * 执行查询语句，返回一个对象列表
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getObjectLists(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<T> beans = null;
        try {
            // 得到数据库连接对象，得到Statement对象
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
            // 替换占位符
            replacePlaceHolder(statement, sql, args);
            // 执行SQL，获得结果集
            resultSet = statement.executeQuery();
            // 从结果集中读取信息
            beans = getBeansFromResultSet(resultSet, clazz);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseConnection(resultSet, statement, connection);
        }
        return beans;
    }

    /**
     * 从结果集中得到对象列表
     *
     * @param resultSet
     * @param clazz
     * @param <T>
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private <T> List<T> getBeansFromResultSet(ResultSet resultSet, Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> beans = null;
        // 通过结果集获得列的别名
        List<String> labels = getColumnLabels(resultSet);
        beans = new ArrayList<>();
        // 处理结果集，得到对象数据
        while (resultSet.next()) {
            T bean = clazz.newInstance();
            for (String label : labels) {
                Object value = resultSet.getObject(label);
                // 使用工具类BeanUtils,为对象赋值
                BeanUtils.setProperty(bean, label, value);
            }
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 执行 insert, delete 和 update 的操作
     *
     * @version 1.0
     * @param sql
     * @param args
     */
    @Deprecated
    public void update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 得到数据库连接对象和Statement
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
            // 替换占位符
            replacePlaceHolder(statement, sql, args);
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
     * 查询某个字段值或一个统计结果
     *
     * @param sql
     * @param args
     * @param <E>
     * @return
     */
    public <E> E getValue(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 连接数据库，获得Statement
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
            // 替换占位符
            replacePlaceHolder(statement, sql, args);
            // 执行SQL
            resultSet = statement.executeQuery();
            //
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用来执行 insert, delete 和 Update 操作
     *
     * @verison 2.0
     * @param connection
     * @param sql
     * @param args
     */
    public void update(Connection connection, String sql, Object... args) {
        PreparedStatement statement = null;
        try {
            // 获得Statement对象
            statement = connection.prepareStatement(sql);
            // 替换占位符
            replacePlaceHolder(statement, sql, args);
            // 执行SQL
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.releaseConnection(statement, null);
        }
    }
}
