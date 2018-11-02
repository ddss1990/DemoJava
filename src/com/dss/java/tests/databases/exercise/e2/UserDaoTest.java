package com.dss.java.tests.databases.exercise.e2;

import com.dss.java.tests.databases.bean.User;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * FileName: UserDaoTest
 * Author: Chris
 * Date: 2018/11/2 11:57
 * Description: User Dao Test
 */
public class UserDaoTest {
    @Test
    public void testQuery() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            UserDao dao = new UserDao();
            String sql = "select _id id, name, balance from users";
            List<Users> users = dao.queryList(connection, sql);
            System.out.println("users = " + users);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(connection);
        }
    }

    @Test
    public void testQueryNoConn() {
        DataSource dataSource = JDBCUtils.getDataSource();
        UserDao dao = new UserDao(dataSource);
        String sql = "select _id id, name, balance from users";
        List<Users> users = dao.queryList(sql);
        System.out.println("users = " + users);
    }
}
