package com.dss.java.tests.databases.exercise.e2;

import com.dss.java.tests.databases.utils.ReflectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * FileName: JDBCDaoImp
 * Author: Chris
 * Date: 2018/11/2 10:34
 * Description: DAO 的实现类
 */
public class JDBCDaoImp<T> implements DAO<T> {

    private final QueryRunner mQueryRunner;
    private Class<T> mType;

    public JDBCDaoImp() {
        mQueryRunner = new QueryRunner();
        mType = ReflectionUtils.getSuperClassGenericType(getClass());
    }

    public JDBCDaoImp(DataSource source) {
        mQueryRunner = new QueryRunner(source);
        mType = ReflectionUtils.getSuperClassGenericType(getClass());
    }

    @Override
    public int update(String sql, Object... args) {
        int update = 0;
        try {
            update = mQueryRunner.update(sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update;
    }

    @Override
    public int update(Connection connection, String sql, Object... args) {
        int update = 0;
        try {
            update = mQueryRunner.update(connection, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return update;
    }

    @Override
    public Object insert(String sql, Object... args) {
        Object insert = null;
        try {
            insert = mQueryRunner.insert(sql, new JDBCResultSetHandler(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert;
    }

    @Override
    public Object insert(Connection connection, String sql, Object... args) {
        Object insert = null;
        try {
            insert = mQueryRunner.insert(connection, sql, new JDBCResultSetHandler(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert;
    }

    @Override
    public T queryObject(String sql, Object... args) {
        T query = null;
        try {
            query = mQueryRunner.query(sql, new BeanHandler<>(mType), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    public T queryObject(Connection connection, String sql, Object... args) {
        T query = null;
        try {
            query = mQueryRunner.query(connection, sql, new BeanHandler<>(mType), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    public List<T> queryList(String sql, Object... args) {
        List<T> query = null;
        try {
            query = mQueryRunner.query(sql, new BeanListHandler<>(mType), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    public List<T> queryList(Connection connection, String sql, Object... args) {
        List<T> query = null;
        try {
            query = mQueryRunner.query(connection, sql, new BeanListHandler<>(mType), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    public <E> E getValue(String sql, Object... args) {
        Object query = null;
        try {
            query = mQueryRunner.query(sql, new ScalarHandler<>(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (E) query;
    }

    @Override
    public <E> E getValue(Connection connection, String sql, Object... args) {
        Object query = null;
        try {
            query = mQueryRunner.query(connection, sql, new ScalarHandler<>(), args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (E) query;
    }

    class JDBCResultSetHandler implements ResultSetHandler {

        @Override
        public Object handle(ResultSet rs) throws SQLException {
            return rs.getObject(1);
        }
    }
}
