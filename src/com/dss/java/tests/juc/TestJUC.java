package com.dss.java.tests.juc;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * FileName: TestJUC
 * Author: Chris
 * Date: 2018/11/5 10:13
 * Description: Test Volatile
 */
public class TestJUC {

    public static void main(String[] args) {


        // 测试 volatile 和 可见性
        //testVolatile();

        /**
         * 测试 Atomic
         * 使用 Atomic 修饰的变量，效率要高于 synchronized 和 volatile 同时修饰的变量
         */

/*        AtomicThread at = new AtomicThread();
        for (int i = 0; i < 10; i++) {
            new Thread(at).start();
        }*/


        // 测速闭锁 - CountDownLatch
        //testCountDownLatch();

        // 测试创建线程的方式之三 - Callable
        //testCallable();

        // 测试生产者消费者
//        testProducerAndConsumer();

    }

    /**
     * 生产者 - 消费者
     */
    private static void testProducerAndConsumer() {
        // 获得锁 - Lock/
        // 功能类似于同步代码块，在需要同步的代码前加锁，代码后释放锁来达到同步的作用
        ReentrantLock reentrantLock = new ReentrantLock();
        // 加锁
        reentrantLock.lock();
        // 释放锁
        reentrantLock.unlock();
        // 通过锁获得Condition，用于线程通信，对线程进行等待和唤醒操作
        Condition condition = reentrantLock.newCondition();
        try {
            // 类似于 Object 的 wait() 方法
            condition.await();
            // 类似于 Object 的 notify() 方法
            condition.signal();
            // 类似于 Object 的 notifyAll() 方法
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 为了避免虚假唤醒，应将 wait() 和 await() 方法放在循环中
    }

    /**
     * 创建线程的方式之三 - Callable
     */
    private static void testCallable() {

        DemoCallable demoCallable = new DemoCallable();
        // 定义一个FutureTask用于接收结果
        FutureTask<Integer> futureTask = new FutureTask<>(demoCallable);
        // 执行子线程
        new Thread(futureTask, "DemoCallable").start();

        Integer sum = null;
        try {
            // 获得Callable的执行结果
            // 会等待子线程执行完毕，才会执行这里，也相当于闭锁
            // 可以在主线程中与子线程中，同时进行计算，在结束的时候，通过get()方法获得子线程的结果，再与主线程的配合进行后续操作
            sum = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("sum = " + sum);
    }

    /**
     * 测试CountDownLatch - 闭锁
     * 计算多线程执行时间
     */
    private static void testCountDownLatch() {
        // 控制多少个线程，参数就传入多少
        CountDownLatch countDownLatch = new CountDownLatch(5);
        LatchThread latchThread = new LatchThread(countDownLatch);
        // 计算多线程执行时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            new Thread(latchThread).start();
        }
        try {
            // 主线程需要进入等待状态，等待其它线程执行完再执行
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("闭锁耗时: " + (end - start));
    }

    /**
     * 验证内存可见性 - 当多个线程操作共享数据时，彼此不可见
     * 应该出现的情况：在子线程中修改了flag，但是由于是延时修改的， 所以当主线程通过getFlag()方法读取到缓存中时应是修改前的结果，应该只打印出子线程中的内容
     * 实际效果：无论是通过@Test还是main()方法中操作的结果都会将子线程和主线程的结果打印，所以不知道是JDK的问题还是其它问题
     */
    public static void testVolatile() {
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
        Hashtable<String, Object> hashTable = new Hashtable<>();
        Map<String, Object> synchronizedMap = Collections.synchronizedMap(map);
        //
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
//        concurrentHashMap.put()
//        CopyOnWriteArraySet
    }

    /**
     * 使用 Callable 的方式来实现线程
     * 与Runnable不同点
     * 1. 有返回值，返回值是定义的泛型
     * 2. 可以抛出异常
     */
    static class DemoCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int i = 0; i < 10000; i++) {
                sum += i;
            }
            System.out.println(Thread.currentThread().getName());
            return sum;
        }
    }

    /**
     * 测试闭锁
     */
    static class LatchThread implements Runnable {
        //
        private CountDownLatch mLatch;

        public LatchThread(CountDownLatch latch) {
            mLatch = latch;
        }

        @Override
        public void run() {
            // 做一些耗时操作
            try {
                for (int i = 0; i < 10000; i++) {
                    if (i % 400 == 0) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                // 加上同步，避免并发操作时同时操作
                synchronized (this) {
                    // 当线程执行结束，对数量进行减少
                    // 最好放在finally中，因为是不管发生什么异常，当线程结束时，此块代码必须执行
                    mLatch.countDown();
                }
            }

        }
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
