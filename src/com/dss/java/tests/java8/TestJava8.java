package com.dss.java.tests.java8;

import com.dss.java.tests.java8.bean.Employee;
import com.dss.java.tests.java8.filter.FilterByAgeImpl;
import com.dss.java.tests.java8.filter.inter.MyFilter;
import org.junit.Test;

import java.util.*;

/**
 * FileName: TestJava8
 * Author: Chris
 * Date: 2018/11/20 11:32
 * Description: Test Java 8 new features
 */
public class TestJava8 {

    private int mId;

    @Test
    /**
     * Lambda 表达式
     */
    public void testLambda() {
        // 内部类方式实现Comparator
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        TreeSet<Integer> treeSet = new TreeSet<>(comparator);

        // Lambda 表达式实现
        Comparator<Integer> comparator1 = (x, y) -> Integer.compare(x, y);
        TreeSet<Integer> treeSet1 = new TreeSet<>(comparator1);
    }

    @Test
    /**
     * 策略设计模式
     */
    public void testPolicyDesign() {
        List<Employee> employees = Arrays.asList(
                new Employee("Tom", 18, 9333),
                new Employee("Jerry", 28, 2643),
                new Employee("Zhang3", 38, 4233),
                new Employee("Li4", 48, 23342)
        );
        // 不使用策略设计，会针对每个条件都创建一个方法
        // 年龄大于30岁
        List<Employee> employees1 = filterEmpByAge(employees);
        System.out.println("employees1 = " + employees1);
        // 工资小于10000
        List<Employee> employees2 = filterEmpBySalary(employees);
        System.out.println("employees2 = " + employees2);

        // 使用策略设计模式，只需要创建一个方法即可
        List<Employee> employees3 = filterEmp(employees, new FilterByAgeImpl());
        System.out.println("employees3 = " + employees3);
        // 上面需要为每个条件创建一个类，使用匿名内部类进行优化，省却了创建类，可读性较差
        List<Employee> employees4 = filterEmp(employees, new MyFilter<Employee>() {
            @Override
            public boolean filter(Employee employee) {
                return employee.getSalary() < 10000;
            }
        });
        // 使用 Lambda 替代匿名内部类
        List<Employee> employees5 = filterEmp(employees, employee -> employee.getSalary() < 10000);

        System.out.println("----------------------");
        // 上边的方式还是需要创建接口，只是不用创建接口的实现类，还可以继续优化
        employees.stream()
                .filter(employee -> employee.getAge() > 30)
                .forEach(System.out::println);
        System.out.println("----------------------");
        employees.stream()
//                .map(employee -> employee.getName())
                .map(Employee::getName)
                .forEach(System.out::println);
    }

    /**
     * 比较方法，会根据传入的Filter的不同，去执行具体的不同比较条件
     *
     * @param employees
     * @param filter
     * @return
     */
    private List<Employee> filterEmp(List<Employee> employees, MyFilter<Employee> filter) {
        List<Employee> list = new ArrayList<>();
        for (Employee e : employees) {
            if (filter.filter(e)) list.add(e);
        }
        return list;
    }

    /**
     * 工资小于10000
     *
     * @param employees
     * @return
     */
    private List<Employee> filterEmpBySalary(List<Employee> employees) {
        List<Employee> list = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getSalary() < 10000) list.add(e);
        }
        return list;
    }

    /**
     * 年龄大于30
     *
     * @param employees
     * @return
     */
    private List<Employee> filterEmpByAge(List<Employee> employees) {
        List<Employee> list = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getAge() > 30) {
                list.add(e);
            }
        }
        return list;
    }

    @Test
    /**
     * 一个Lambda运算
     */
    public void testCalc() {
//        (x,y,z) -> x+y+z
        // 一个简单的Lambda使用
        new Thread(() -> method(), "Sub1").start();

        // 1. 首先要定一个一个函数式接口 - MyCalc
        // 2. 定义一个方法，需要使用到接口 - calc()
        // 3. 调用方法
        Double result = calc(100.0, (x) -> Math.sqrt(x));
        // 4. 输出结果
        System.out.println("result = " + result);
    }

    private void method() {
        System.out.println("-----------------");
        System.out.println(Thread.currentThread().getName() + " lambda");
    }

    private <T> T calc(T t, MyCalc<T> calc) {
        return calc.calc(t);
    }

}

@FunctionalInterface
interface MyCalc<T> {
    T calc(T t);
}
