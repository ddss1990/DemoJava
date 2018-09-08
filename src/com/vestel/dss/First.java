package com.vestel.dss;

import com.vestel.dss.bean.Cat;

/**
 * FileName: First
 * Author: Chris
 * Date: 2018/9/3 11:39
 * Description: First class
 */
public class First {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        double pi = Math.PI;
        Person person;
        int flag = Person.FLAG;
        System.out.println("flag = " + flag);
        System.out.println("-------------------");
        person = new Person("Tom", 19);
        System.out.println("person = " + person);
        person.name = "Jerry";
        person.age = 18;
        System.out.println("person = " + person);
        Person person1 = new Person();
        System.out.println("person1 = " + person1);
        Cat cat = new Cat();
    }
}

class Person implements Listener {
    String name;
    int age;
    public static final int FLAG = 0;
//    public static final String TAG;
    public final int TEST_FINAL;

    {
//        TAG = "";
        TEST_FINAL = 1;
        name = "Jack";
        age = 10;
        System.out.println("非静态代码块");
    }

    static {
        System.out.println("静态代码块");
    }

    public Person() {

    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int addOne(final int x) {
        return x;
    }


    @Override
    public String toString() {
        return "Name = " + name + ", age = " + age;
    }

    @Override
    public void onClick() {

    }
}

interface Listener {
    void onClick();
}

