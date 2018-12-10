package com.dss.web.test;

import com.dss.web.DBManager;
import com.dss.web.bean.Employee;
import com.dss.web.bean.Location;
import com.dss.web.dao.BaseDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * User: DSS
 * Date: 2018/12/10
 * Time: 17:46
 * Description: Test1 Base
 */
public class TestBase {
    public static void main(String[] args) {
//        Connection connection = DBManager.getInstance().getConnection();
//        System.out.println("connection = " + connection);
//        QueryRunner queryRunner = new QueryRunner();
        /*String sql = "select location_id locationID, city from locations;";
        BaseDao dao = new BaseDao();
        List<Location> list = null;
        //            list = queryRunner.query(connection, sql, new BeanListHandler<>(Location.class));
        list = dao.getForList(sql, Location.class);
        System.out.println("list = " + list);*/

//        int employeeId = 114;
        String employeeId = "114";
        System.out.println("employeeId = " + employeeId);
        String sql = "select employee_id employeeId, last_name lastName, email, salary from employees where employee_id = ?";
        BaseDao dao = new BaseDao();
        Employee employee = dao.get(sql, Employee.class, employeeId);
        System.out.println("employee = " + employee);
    }

    @Test
    public void test() {
//        String employeeId = request.getParameter("employeeId");
        String employeeId = "114";
        System.out.println("employeeId = " + employeeId);
        String sql = "select employee_id employeeId, last_name lastName, email, salary from employees where employee_id = ?";
        BaseDao dao = new BaseDao();
        Employee employee = dao.get(sql, Employee.class, employeeId);
        System.out.println("employee = " + employee);
    }
}
