package com.dss.java.tests.databases.exercise.e2;

import javax.sql.DataSource;

/**
 * FileName: UserDao
 * Author: Chris
 * Date: 2018/11/2 11:56
 * Description: User DAO
 */
public class UserDao extends JDBCDaoImp<Users> {
    public UserDao() {

    }

    public UserDao(DataSource source) {
        super(source);
    }
}
