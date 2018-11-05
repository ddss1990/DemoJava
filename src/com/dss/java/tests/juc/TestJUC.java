package com.dss.java.tests.juc;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FileName: TestJUC
 * Author: Chris
 * Date: 2018/11/5 10:13
 * Description: Test Volatile
 */
public class TestJUC {

    public static void main(String[] args) {

        /**
         * 测试 volatile 和 可见性

         // 创建一个子线程，并启动
         MyThread myThread = new MyThread();
         Thread thread = new Thread(myThread);
         //thread.setName("Sub");
         thread.start();

         while (true) {
         // 如果子线程有修改flag，就打印
         if (myThread.getFlag()) {
         //            if (myThread.flag) {
         System.out.println("---------------");
         break;
         }
         }
         */
        /**
         * 测试 Atomic
         * 使用 Atomic 修饰的变量，效率要高于 synchronized 和 volatile 同时修饰的变量
         */
        AtomicThread at = new AtomicThread();
        for (int i = 0; i < 10; i++) {
            new Thread(at).start();
        }
    }

    /**
     * 验证内存可见性 - 当多个线程操作共享数据时，彼此不可见
     * 应该出现的情况：在子线程中修改了flag，但是由于是延时修改的， 所以当主线程通过getFlag()方法读取到缓存中时应是修改前的结果，应该只打印出子线程中的内容
     * 实际效果：无论是通过@Test还是main()方法中操作的结果都会将子线程和主线程的结果打印，所以不知道是JDK的问题还是其它问题
     */
    @Test
    public void testVolatile() {
        // 创建一个子线程，并启动
        MyThread myThread = new MyThread();
        Thread thread = new Thread(myThread);
        thread.setName("Sub");
        thread.start();

        while (true) {
            // 如果子线程有修改flag，就打印
            if (myThread.getFlag()) {
                System.out.println("---------------");
            }
        }
    }

    /**
     * 测试原子性
     * 在Test模块中循环创建子线程并开始发现有遗漏现象，也就是10个线程没有全部都有打印
     */
    @Test
    public void testAtomic() {
        int i = 10;
        int a = i++;
        int b = ++i;
        System.out.println("a = " + a + ", b = " + b);

        AtomicThread atomic = new AtomicThread();
        for (int j = 0; j < 10; j++) {
            new Thread(atomic).start();
        }
    }

    /**
     * 测试ConcurrentHashMap
     */
    @Test
    public void testConcurrentHashMap() {
        // 传统方法创建一个并发的HashMap
        HashMap<String, Object> map = new HashMap<>();
        Map<String, Object> synchronizedMap = Collections.synchronizedMap(map);
        //
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
//        CopyOnWriteArraySet
    }

    /**
     * 模拟CAS算法
     * expectedValue - 预估值，是程序执行前读取到的值
     * oldValue - 内存值，内存中的值，有可能被其它线程修改
     * 只有在 expectedValue == oldValue 时，才能说明此值没有被其它线程修改
     */
    class CompareAndSwap {
        private int value;

        /**
         * 读取内存中的值，需是同步的方法
         *
         * @return
         */
        public synchronized int getValue() {
            return value;
        }

        /**
         * 比较，如果内存值与预估值相等，就修改内存值
         * 不管成功与不成功，都返回旧值
         *
         * @param expectedValue 预估值
         * @param newValue      更新值
         * @return
         */
        public synchronized int comparedAndSwap(int expectedValue, int newValue) {
            // 内存中的值
            int oldValue = value;
            // 判断内存中的值与预期的值是否相等，如果相等就更新值
            if (oldValue == expectedValue) {
                value = newValue;
            }
            // 返回值
            return oldValue;
        }

        public synchronized boolean compareAndSet(int exceptedValue, int newValue) {
            return exceptedValue == comparedAndSwap(exceptedValue, newValue);
        }
    }

    /**
     * 用于演示原子性的子线程，所做的操作只有将变量 serialNumber++ 然后进行打印
     */
    static class AtomicThread implements Runnable {
        // 正常声明变量
//        private int serialNumber = 0;
        // 使用 volatile 修饰，volatile 只是使变量在各个线程之间可见，但是并没有保证独立，多个线程操作的时候仍有可能会产生错乱
//        private volatile int serialNumber = 0;
        // 使用原子变量的方式来声明变量，就会有效的解决冲突问题
        private AtomicInteger serialNumber = new AtomicInteger(0);

        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " ---- " + getSerialNumber());
        }

        public int getSerialNumber() {
            //return serialNumber++;
            return serialNumber.getAndIncrement();
        }
    }

}

class MyThread implements Runnable {
    private boolean flag = false;
//    private volatile boolean flag = false;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {

        try {
            // 加个延迟
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //setFlag(true);
        flag = true;
        System.out.println(Thread.currentThread().getName() + " flag = " + flag);
    }
}
