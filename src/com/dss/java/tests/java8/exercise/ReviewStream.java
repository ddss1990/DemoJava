package com.dss.java.tests.java8.exercise;

import com.dss.java.tests.java8.bean.Employee;
import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: DSS
 * Date: 2018/11/21
 * Time: 22:02
 * Tag: Review Stream
 */
public class ReviewStream {

    List<Employee> employees = Arrays.asList(
            new Employee("Tom", 18, 9333),
            new Employee("Jerry", 28, 2643),
            new Employee("Zhang3", 38, 4233),
            new Employee("Li4", 48, 23342)
    );
    String[] mStrings = new String[10];

    @Test
    public void reviewCreateStream() {
        // 1. 集合流
        Stream<Employee> stream1 = employees.stream();
        // 2. 数组流
        Stream<String> stream2 = Arrays.stream(mStrings);
        // 3. Stream.of
        Stream<String> stream3 = Stream.of(mStrings);
        // 4. 无限流
        // 4.1 迭代流
        Stream<Integer> stream4 = Stream.iterate(0, x -> x + 2);
        // 4.2 生成流
        Stream<Double> stream5 = Stream.generate(() -> Math.random() * 1000);
        Stream<Double> stream6 = Stream.generate(Math::random);
    }

    @Test
    public void reviewEndStream() {
        Stream<Employee> stream = employees.stream();
        long count = stream.map(Employee::getSalary).count();
        System.out.println("count = " + count);
        // reduce - 最大值
        Float aFloat = employees.stream().map(Employee::getSalary).reduce(BinaryOperator.maxBy(Float::compareTo)).get();
        System.out.println("最大值 = " + aFloat);
        Float sum = employees.stream().map(Employee::getSalary).reduce(Float::sum).get();
        System.out.println("sum = " + sum);
        // 收集
        List<String> names = employees.stream().map(Employee::getName).collect(Collectors.toList());
        System.out.println("names = " + names);
        HashSet<String> names_hashSet = employees.stream().map(Employee::getName).collect(Collectors.toCollection(HashSet::new));
        System.out.println("names_hashSet = " + names_hashSet);
        Double avg_salary = employees.stream().collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println("avg_salary = " + avg_salary);
        DoubleSummaryStatistics dss = employees.stream().collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println("dss = " + dss);

        String join1 = employees.stream().map(Employee::getName).collect(Collectors.joining());
        System.out.println("Collectors.joining 无参数 = " + join1);
        String join2 = employees.stream().map(Employee::getName).collect(Collectors.joining("-"));
        System.out.println("Collectors.joining 1个参数 = " + join2);
        String join3 = employees.stream().map(Employee::getName).collect(Collectors.joining("-", "#####", "******"));
        System.out.println("Collectors.joining 3个参数 = " + join3);

    }
}
