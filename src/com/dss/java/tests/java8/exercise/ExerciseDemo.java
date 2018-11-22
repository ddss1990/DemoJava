package com.dss.java.tests.java8.exercise;

import com.dss.java.tests.java8.bean.Employee;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        String s = method2("abcd", str -> str.toUpperCase());
        String s1 = method2("abcd", String::toUpperCase);
        System.out.println("s = " + s);
        String sub = method2("Hello Lambda", str -> str.substring(2, 4));
        System.out.println("sub = " + sub);
    }

    // 2. 声明方法使用接口作为函数
    public String method2(String str, MyInter2 inter2) {
        return inter2.getValue(str);
    }

    // 1. 声明函数式接口
    @FunctionalInterface
    interface MyInter2 {
        public String getValue(String str);
    }


    /**
     * 1. 声明一个带两个泛型的函数式接口<T, R> T 为参数， R为返回值
     * 2. 接口中声明对应抽象方法
     * 3. 在TestLambda中声明方法，使用接口作为参数，计算两个long型参数的和
     * 4. 再计算两个long类型参数的积
     */
    @Test
    public void test3() {
        long l1 = 123412;
        long l2 = 5234;
        Long sum = method3(l1, l2, (p1, p2) -> p1 + p2);
        System.out.println("sum = " + sum);
        Long product = method3(l1, l2, (x, y) -> x * y);
        System.out.println("product = " + product);
    }

    // 3 声明方法，接口作为参数
    public <T, R> R method3(T t1, T t2, MyInter3<T, R> inter3) {
        return inter3.getValue(t1, t2);
    }

    // 1. 声明带两个泛型的函数式接口
    @FunctionalInterface
    interface MyInter3<T, R> {
        // 2. 接口中声明对应抽象方法
        public R getValue(T t1, T t2);
    }

    /**
     * 给定一个数字列表，返回一个由每个数的平方组成的新的列表
     */
    @Test
    public void test4() {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> nums_square = nums.stream().map(x -> x * x).collect(Collectors.toList());
        System.out.println("nums_square = " + nums_square);
    }

    /**
     * 使用map和reduce确定列表中有多少元素
     */
    @Test
    public void test5() {
        Integer sum = employees.stream().map(e -> 1).reduce(Integer::sum).get();
        System.out.println("sum = " + sum);
    }
    ;
}
