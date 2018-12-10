package com.dss.web.servlets;

import com.dss.web.DBManager;
import com.dss.web.bean.Department;
import com.dss.web.bean.Employee;
import com.dss.web.bean.Location;
import com.dss.web.dao.BaseDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

public class EmployeeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String methodName = request.getParameter("method");
//        System.out.println("methodName = " + methodName);
        try {
            Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private void listLocations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "select location_id locationId,city from locations;";
//        System.out.println("sql = " + sql);
        BaseDao dao = new BaseDao();
//        System.out.println("dao = " + dao);
        List<Location> locations = dao.getForList(sql, Location.class);
//        System.out.println("locations = " + locations);

        // 将查询到的结果放到request中
        request.setAttribute("locations", locations);
        request.getRequestDispatcher("/ajax/ajax-7.jsp").forward(request, response);
    }

    private void listDepartments(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String locationId = request.getParameter("locationId");
        String sql = "select department_id departmentId, department_name departmentName from departments where location_id = ?";
        BaseDao dao = new BaseDao();
        List<Department> departments = dao.getForList(sql, Department.class, locationId);
        System.out.println("departments = " + departments);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(departments);

        response.setContentType("text/javascript");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(json);
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String departmentId = request.getParameter("departmentId");
        String sql = "select employee_id employeeId, last_name lastName from employees where department_id = ?";
        BaseDao dao = new BaseDao();
        List<Employee> employees = dao.getForList(sql, Employee.class, departmentId);
        System.out.println("employees = " + employees);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employees);

        response.setContentType("text/javascript");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(json);
    }

    private void listEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String employeeId = request.getParameter("employeeId");
        System.out.println("employeeId = " + employeeId);
        String sql = "select employee_id employeeId, last_name lastName, email, salary from employees where employee_id = ?";
        BaseDao dao = new BaseDao();
        Employee employee = dao.get(sql, Employee.class, employeeId);
        System.out.println("employee = " + employee);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employee);

        response.setContentType("text/javascript");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(json);
    }
}
