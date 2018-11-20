package com.dss.java.tests.java8.bean;

/**
 * FileName: Employee
 * Author: Chris
 * Date: 2018/11/20 15:05
 * Description: Employee Bean
 */
public class Employee {

    private String name;
    private int age;
    private float salary;

    public Employee() {
    }

    public Employee(String name, int age, float salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
