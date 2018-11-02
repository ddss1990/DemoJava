/**
 * FileName: DAO
 * Author: Chris
 * Date: 2018/11/2 10:28
 * Description: DAO
 */
package com.dss.java.tests.databases.exercise.e2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public interface DAO<T> {

    int update(String sql, Object... args);

    int update(Connection connection, String sql, Object... args);

    Object insert(String sql, Object... args);

    Object insert(Connection connection, String sql, Object... args);

    T queryObject(String sql, Object... args);

    T queryObject(Connection connection, String sql, Object... args);

    List<T> queryList(String sql, Object... args);

    List<T> queryList(Connection connection, String sql, Object... args);

    <E> E getValue(String sql, Object... args);

    <E> E getValue(Connection connection, String sql, Object... args);
}
