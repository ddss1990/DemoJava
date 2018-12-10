package com.dss.web.dao;

import com.dss.web.DBManager;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * User: DSS
 * Date: 2018/12/10
 * Time: 17:16
 * Description: Base Dao
 */
public class BaseDao {
    private static final QueryRunner runner = new QueryRunner();

    /**
     * 查询数据库，得到集合对象
     *
     * @param sql
     * @param clazz
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getForList(String sql, Class<T> clazz, Object... args) {
        List<T> list = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            list = runner.query(connection, sql, new BeanListHandler<T>(clazz), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(connection);
        }
        return list;
    }

    public <T> T get(String sql, Class<T> clazz, Object... args) {
        T result = null;
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            result = runner.query(connection, sql, new BeanHandler<>(clazz), args);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.releaseConnection(connection);
        }
        return result;
    }
}
