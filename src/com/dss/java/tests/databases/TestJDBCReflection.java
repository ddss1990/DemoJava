package com.dss.java.tests.databases;

import com.dss.java.tests.databases.bean.Student;
import com.dss.java.tests.databases.bean.User;
import com.dss.java.tests.databases.utils.JDBCUtils;
import com.dss.java.tests.databases.utils.ReflectionUtils;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: TestJDBCReflection
 * Author: Chris
 * Date: 2018/10/25 10:34
 * Description: 通过反射创建通用的查询方法
 */
public class TestJDBCReflection {
    @Test
    public void testGetStudent() {
        getStudent("");
    }

    /**
     * 查询Student
     */
    public void getStudent(String sql, Object... args) {
        Connection connection = null;
        try {
            // 1. 获得数据库连接
            connection = JDBCUtils.getConnection();
            // 2. 准备SQL语句
            sql = "select * from examstudent;";
            //3. 获得PrepareStatement对象
            PreparedStatement statement = connection.prepareStatement(sql);
            // 4. 执行
            ResultSet resultSet = statement.executeQuery();
            // 5. 从结果集中获得数据
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int type = resultSet.getInt(2);
                String idCard = resultSet.getString(3);
                String examCard = resultSet.getString(4);
                String name = resultSet.getString(5);
                String location = resultSet.getString(6);
                int grade = resultSet.getInt(7);
                Student student = new Student(id, type, idCard, examCard, name, location, grade);
                System.out.println("student = " + student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUser() {
        getUser();
    }

    /**
     * 查询User
     */
    public void getUser() {
        Connection connection = null;
        String sql = "select * from test_jdbc_users";
        PreparedStatement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);
                System.out.println("User [ id = " + id + ", name = " + name + ", email = " + email + ", birth = " + birth + "]");
            }
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
     * 通用的查询数据库，返回实体类
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    private <T> List<T> getObject(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        List list = new ArrayList<T>();
        T bean = null;
        try {
            // 连接数据库，获得Statement对象，得到结果集
            connection = JDBCUtils.getConnection();
            statement = connection.prepareStatement(sql);
            // 替换占位符
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            resultSet = statement.executeQuery();
            // 2. 通过结果集获取元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            System.out.println("count = " + count);
            // 解析结果集和表头
            while (resultSet.next()) {
                bean = clazz.newInstance();
                for (int i = 0; i < count; i++) {
                    // 获得每一列的表头和数据
                    String label = metaData.getColumnLabel(i + 1);
                    Object object = resultSet.getObject(label);
                    //System.out.println("label = " + label + ", value = " + object);
                    ReflectionUtils.setFieldValue(bean, label, object);
                }
                list.add(bean);
            }
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
        } finally {
            // 释放资源
            JDBCUtils.releaseConnection(resultSet, statement, connection);
        }
        return list;
    }

    @Test
    public void testGetObject() {
        String sql = "select _id id, name name, email email, birth birth from test_jdbc_users";
        List<User> list = getObject(User.class, sql);
        System.out.println("list = " + list);
        String sql1 = "select _id id, name name, email email, birth birth from test_jdbc_users where _id = ?";
        List<User> list1 = getObject(User.class, sql1, 1005);
        System.out.println("list1 = " + list1);
        String sql2 = "select FlowId flowID, Type type, IDCard mIDCard, ExamCard examCard, " +
                "StudentName studentName, Location location, Grade grade from examstudent";
        List<Student> students = getObject(Student.class, sql2);
        System.out.println("students = " + students);
        String sql3 = "select FlowId flowID, Type type, IDCard mIDCard, ExamCard examCard, " +
                "StudentName studentName, Location location, Grade grade from examstudent where flowID = ?";
        List<Student> student = getObject(Student.class, sql3, 5);
        System.out.println("student = " + student);
    }
}
