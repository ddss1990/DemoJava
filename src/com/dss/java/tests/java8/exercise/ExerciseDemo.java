package com.dss.java.tests.java8.exercise;

import com.dss.java.tests.java8.bean.Employee;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * FileName: ExerciseDemo
 * Author: Chris
 * Date: 2018/11/20 17:37
 * Description: Exercise
 */
public class ExerciseDemo {

    List<Employee> employees = Arrays.asList(
            new Employee("Tom", 18, 9333),
            new Employee("Jerry", 28, 2643),
            new Employee("Zhang3", 38, 4233),
            new Employee("Li4", 48, 23342)
    );

    @Test
    /**
     * 调用Collections.sort() 方法，通过定制排序对比两个Employee(先按年龄比，在按姓名比)，使用Lambda作为参数传递
     */
    public void test1() {
        // 只比较一个条件的情况下，上边的语句是Idea针对Lambda推荐的修改，更简便
//        Collections.sort(employees, Comparator.comparingInt(Employee::getAge));
//        Collections.sort(employees, (e1, e2) -> Integer.compare(e1.getAge(), e2.getAge()));
        Collections.sort(employees, (e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return Integer.compare(e1.getAge(), e2.getAge());
            }
        });
        System.out.println("employees = " + employees);
    }

    @Test
    /**
     * 1. 声明函数式接口，接口中声明抽象方法 public String getValue(String str)
     * 2. 声明类 TestLambda，类中编写方法使用接口作为参数，将一个字符串转换为大写，并作为结果返回
     * 3. 再将一个字符串的第2个和第4个索引位置进行截取子串
     */
    public void test2() {

    }


    /**
     * 1. 声明一个带两个泛型的函数式接口<T, R> T 为参数， R为返回值
     * 2. 接口中声明对应抽象方法
     * 3. 在TestLambda中声明方法，使用接口作为参数，计算两个long型参数的和
     * 4. 再计算两个long类型参数的积
     */
}
