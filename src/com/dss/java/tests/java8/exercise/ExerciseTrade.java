package com.dss.java.tests.java8.exercise;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FileName: ExerciseTrade
 * Author: Chris
 * Date: 2018/11/22 10:05
 * Description: Exercise Trade
 */
public class ExerciseTrade {
    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "MiLan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");
    List<Transaction> mTransactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950));

    //1. 找出2011年发生的所有交易， 并按交易额排序（从低到高）
    @Test
    public void test1() {
        mTransactions.stream().filter(t -> t.getYear() == 2011).sorted((t1, t2) -> Integer.compare(t1.getValue(), t2.getValue())).forEach(System.out::println);
    }

    //2. 交易员都在哪些不同的城市工作过？
    @Test
    public void test2() {
        mTransactions.stream().map(Transaction::getTrader).map(Trader::getCity).distinct().forEach(System.out::println);
    }

    //3. 查找所有来自剑桥的交易员，并按姓名排序
    @Test
    public void test3() {
        mTransactions.stream()
                .map(Transaction::getTrader)
                .filter(t -> t.getCity().equals("Cambridge"))
                .distinct()
                .sorted((t1, t2) -> t1.getName().compareTo(t2.getName()))
                .forEach(System.out::println);
        ;
    }

    //4. 返回所有交易员的姓名字符串，按字母顺序排序
    @Test
    public void test4() {
        mTransactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .flatMap(this::filterStr)
                .sorted().forEach(character -> System.out.print(character + " "));
    }

    public Stream<Character> filterStr(String str) {
        List<Character> characters = new ArrayList<>();
        for (Character cha : str.toCharArray()) {
            characters.add(cha);
        }
        return characters.stream();
    }

    //5. 有没有交易员是在米兰工作的？
    @Test
    public void test5() {
        mTransactions.stream()
                .map(Transaction::getTrader)
                .distinct()
                .filter(trader -> trader.getCity().equals("MiLan"))
                .forEach(System.out::println);
        boolean miLan = mTransactions.stream()
                .map(Transaction::getTrader)
                .distinct().anyMatch(t -> t.getCity().equals("MiLan"));
        System.out.println("miLan = " + miLan);
    }

    //6. 打印生活在剑桥的交易员的所有交易额
    @Test
    public void test6() {
        mTransactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .forEach(transaction -> System.out.println(transaction.getTrader().getName() + "  " + transaction.getValue()));
        Map<String, List<Transaction>> cambridge = mTransactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .collect(Collectors.groupingBy(transaction -> transaction.getTrader().getName()));
        System.out.println("cambridge = " + cambridge);
        Integer sum = mTransactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .reduce(Integer::sum).get();
        System.out.println("sum = " + sum);
    }
    //7. 所有交易中，最高的交易额是多少
    @Test
    public void test7() {
        Integer max = mTransactions.stream()
                .map(Transaction::getValue)
//                .max(Integer::compare)
                .max(Integer::compareTo).get();
        System.out.println("max = " + max);
    }

    //8. 找到交易额最小的交易
    @Test
    public void test8() {
        Transaction transaction = mTransactions.stream()
                .min((o1, o2) -> Integer.compare(o1.getValue(), o2.getValue())).get();
        System.out.println("transaction = " + transaction);
    }
}

/**
 * 交易员类
 */
class Trader {
    private String name;
    private String city;

    public Trader() {
    }

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Trader{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

/**
 * 交易类
 */
class Transaction {
    private Trader trader;
    private int year;
    private int value;

    public Transaction() {
    }

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "trader=" + trader +
                ", year=" + year +
                ", value=" + value +
                '}';
    }
}
