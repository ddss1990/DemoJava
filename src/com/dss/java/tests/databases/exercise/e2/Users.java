package com.dss.java.tests.databases.exercise.e2;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * FileName: Users
 * Author: Chris
 * Date: 2018/11/2 11:55
 * Description: Bean : Users
 */
public class Users {
    private int mId;
    private String mName;
    private double mBalance;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getBalance() {
        return mBalance;
    }

    public void setBalance(double balance) {
        mBalance = balance;
    }

    @Override
    public String toString() {
        return "Users{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mBalance=" + mBalance +
                '}';
    }

    @Test
    public void testReflection() {
        Type type = C.class.getGenericSuperclass();
        System.out.println("type = " + type);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        System.out.println("types = " + Arrays.toString(types));
    }

    class A<T> {
    }

    class B<T> extends A<T> {
    }

    class C extends B<Users> {
    }
}
