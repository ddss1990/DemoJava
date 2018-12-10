package com.dss.web.bean;

/**
 * User: DSS
 * Date: 2018/12/10
 * Time: 17:41
 * Description: Employee Bean
 */
public class Employee {
    private Integer departmentId;
    private Integer employeeId;
    private String lastName;
    private double salary;
    private String email;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "departmentId=" + departmentId +
                ", employeeId=" + employeeId +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", email='" + email + '\'' +
                '}';
    }
}
