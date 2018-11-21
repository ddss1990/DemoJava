package com.dss.java.tests.java8;

import com.dss.java.tests.java8.bean.Employee;
import com.dss.java.tests.java8.filter.FilterByAgeImpl;
import com.dss.java.tests.java8.filter.inter.MyFilter;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    List<Employee> employees = Arrays.asList(
            new Employee("Tom", 18, 9333),
            new Employee("Jerry", 28, 2643),
            new Employee("Zhang3", 38, 4233),
            new Employee("Li4", 48, 23342)
    );

    @Test
    /**
     * 策略设计模式
     */
    public void testPolicyDesign() {
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

    /**
     * 核心接口
     * Consumer - 消费型接口，有参无返回值
     * Supplier - 供给型接口，无参有返回值
     * Function - 方法型接口，有参有返回值
     * Predicate - 断言型接口，有参返回值为boolean
     */
    @Test
    public void testCoreFunInter() {
        // 1. 消费型接口
        Consumer consumer;
        methodConsumer("Hello world", s -> {
            System.out.println("\t\t" + s);
        });
        methodConsumer("Tom", s -> System.out.println(s + " is my son!"));
        // 2. 供给型接口
        Supplier supplier;
        List<Integer> list = methodSupplier(10, () -> (int) (Math.random() * 100));
        System.out.println("list = " + list);
        // 3. 方法型接口
        Function function;
        String s1 = methodFunction("Hello world", s -> s.substring(2));
        System.out.println("s1 = " + s1);
        String s2 = methodFunction("\t  Hello world \t\t   ", s -> s.trim());
        System.out.println("s2 = " + s2);
        // 4. 断言型接口
        Predicate predicate;
        List<Employee> list1 = methodPredicate(employees, e -> e.getName().length() > 3);
        System.out.println("list1 = " + list1);
        List<Employee> list2 = methodPredicate(employees, e -> e.getAge() < 30);
        System.out.println("list2 = " + list2);
    }

    /**
     * Consumer - 消费型接口
     * 使用场景举例 : 对传入的字符串进行定制化输出
     */
    private void methodConsumer(String str, Consumer<String> consumer) {
        consumer.accept(str);
    }

    /**
     * Supplier - 供给型接口
     * 使用场景举例 - 产生指定个数的整数，放入到集合中
     */
    private List<Integer> methodSupplier(int num, Supplier<Integer> supplier) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    /**
     * Function - 方法型接口
     * 使用场景举例：处理字符串
     */
    private String methodFunction(String str, Function<String, String> function) {
        return function.apply(str);
    }

    /**
     * Predicate - 断言型接口
     * 使用场景举例：处理集合，返回满足条件的集合
     */
    private <T> List<T> methodPredicate(List<T> lists, Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T t : lists) {
            if (predicate.test(t)) list.add(t);
        }
        return list;
    }

    /**
     * 方法引用
     */
    @Test
    public void testMethodRef() {
        // 1. 对象::实例方法名
        Consumer<String> consumer = s -> System.out.println("s = " + s);
        Consumer<String> consumer2 = System.out::println;
        consumer2.accept("Hello world!");
        // 2 类::静态方法名
        Comparator<Integer> comparator1 = (x, y) -> Integer.compare(x, y);
        Comparator<Integer> comparator2 = Integer::compare;
        Comparator<Integer> comparator3 = Comparator.comparingInt(x -> x);
        // 3. 类::实例方法名
        BiPredicate predicate1 = (x, y) -> x.equals(y);
        BiPredicate predicate2 = Object::equals;
        Object t = new Object(), u = new Object();
        predicate2.test(t, u);
        Function<String, String> function2 = new Function<String, String>() {
            @Override
            public String apply(String s) {
                return null;
            }
        };
        Function<Employee, String> function1 = employee -> employee.getName();
        Function<? super Employee, ?> function = Employee::getAge;
        employees.stream().map(function);
        System.out.println(function1.apply(employees.get(0)));
    }

    /**
     * 构造引用
     */
    @Test
    public void testConstructorRef() {
        Supplier<Employee> supplier1 = () -> new Employee();
        Supplier<Employee> supplier2 = Employee::new;
        Employee employee = supplier2.get();
    }

    /**
     * 数组引用
     */
    @Test
    public void testArrayRef() {
        Function<Integer, String[]> function1 = n -> new String[n];
        Function<Integer, String[]> function2 = String[]::new;
        String[] strings = function2.apply(10);
    }

    /**
     * Stream
     */
    @Test
    public void testCreateStream() {
        // 流的多种创建方式
        // 1. 集合流
        Stream<Employee> stream1 = employees.stream();
        // 2. 数组流
        int[] bytes = new int[1024];
        IntStream stream2 = Arrays.stream(bytes);
        // 3. 静态方法获取
        String[] strs = new String[100];
        Stream<String> stream3 = Stream.of(strs);
        Stream<Integer> stream4 = Stream.of(1, 2, 3, 4, 5);
        // 4. 无限流
        // 4.1 迭代式
        Stream<Integer> stream5 = Stream.iterate(2, x -> x + 2);
        // 4.2 生成式
        Stream<Integer> stream6 = Stream.generate(() -> ((int) (Math.random() * 100)));
        stream6.limit(10)//限定个数
                .forEach(System.out::println);// 遍历每个元素，然后针对每个元素执行操作
    }

    /**
     * 中间操作
     */
    @Test
    public void testMidOperation() {
        employees.stream().filter(e -> {
            System.out.println("短路!");
            return e.getAge() > 20;
        }).limit(2).forEach(System.out::println);

        // 映射 map & flatMap
        List<String> strings = Arrays.asList("aaa", "bbb", "ccc", "ddd");
        // map - 将每个元素都执行方法，每个元素执行的结果作为流的新元素，每个元素执行方法后的结果为流，所以最终结果为Stream<Stream<Character>>
        Stream<String> stream = strings.stream();
        Stream<Stream<Character>> stream1 = stream.map(this::filterChar);
        // flatMap - 将每个元素转为一个流，并将所有的流连接成一个流，结果为Stream<Character>
        Stream<Character> stream2 = stream.flatMap(this::filterChar);
        // 那如果方法返回的结果不是流呢，map()不会有影响，结果变为Stream<List<Character>>;而flatMap就会报错，因为方法的结果返回值不是流
//        Stream<List<Character>> stream1 = stream.map(this::filterChar);
//        Stream<R> stream2 = stream.flatMap(this::filterChar);
    }

    /**
     * 将一个字符串转为流，流中的元素为Char
     *
     * @param str
     * @return
     */
    private Stream<Character> filterChar(String str) {
//    private List<Character> filterChar(String str) {
        List<Character> list = new ArrayList<>();
        for (Character cha : str.toCharArray()) {
            list.add(cha);
        }
        return list.stream();
//        return list;
    }

    /**
     * 终止操作
     */
    @Test
    public void testEndOperation() {
        // 正序
        employees.stream().sorted(Comparator.comparingDouble(Employee::getSalary)).forEach(System.out::println);
        // 倒序
        employees.stream().sorted((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary())).forEach(System.out::println);
        // findAny
        Employee employee = employees.parallelStream().findAny().get();
        System.out.println("employee = " + employee);
        Double d = 1D;
        d.compareTo(2D);
        Double.compare(1, 2);

        List<String> collect = employees.stream().map(Employee::getName).collect(Collectors.toList());
    }
}

@FunctionalInterface
interface MyCalc<T> {
    T calc(T t);
}
