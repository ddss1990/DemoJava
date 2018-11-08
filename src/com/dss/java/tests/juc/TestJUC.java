package com.dss.java.tests.juc;

import org.junit.Test;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

        // 交替打印ABC
        //testPrintAlternate();

        // 读写锁
        //testReadWriteLock();

        // 八锁
/*        Test8Lock test8Lock = new Test8Lock();
        try {
            test8Lock.begin();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // 线程池
//        testThreadPool();

        // 测试ForkJoin 拆分与合并任务
        TestForkJoin forkJoin = new TestForkJoin();
        forkJoin.start();

        testMap();
    }

    /**
     *  HashMap & ConcurretnHashMap
     */
    private static void testMap() {
        HashMap<String, Object> hashMap = new HashMap();
        Object put = hashMap.put("", "");
        Object o = hashMap.get("");
        Set<Map.Entry<String, Object>> set = hashMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            String key = entry.getKey();
            Object value = entry.getValue();
        }
        Iterator iterator = set.iterator();

        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("", "");
        Set<Map.Entry<String, Object>> entries = concurrentHashMap.entrySet();

    }

    /**
     * 测试线程池
     */
    private static void testThreadPool() {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                }
                return null;
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                }
            }
        };
        // 为线程池分配任务
        for (int i = 0; i < 10; i++) {
            //executorService.submit(callable);
            executorService.submit(runnable);
        }
        // 关闭线程池
        executorService.shutdown();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.schedule(runnable, 3, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
    }

    /**
     * 测试读写锁
     * 可同时多个线程一起读，但不能一起写，也不能一起读和写
     */
    private static void testReadWriteLock() {
        // 创建读写锁对象
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        // 读锁
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        // 写锁
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        int number = 0;

        // 读锁处理
        try {
            readLock.lock();
            System.out.println("number = " + number);
        } finally {
            readLock.unlock();
        }

        // 写锁处理
        try {
            writeLock.lock();
            number = 10;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 使用3个线程，每个线程的名字分别是A,B,C，要求按顺序打印ABC 10次
     */
    private static void testPrintAlternate() {
        /*
        ABCAlternate abc = new ABCAlternate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    abc.printA();
                }
            }
        }, "A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    abc.printB();
                }
            }
        }, "B").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    abc.printC();
                    System.out.println();
                }
            }
        }, "C").start();*/
        ABCThread thread = new ABCThread();
        Thread a = new Thread(thread, "A");
        Thread b = new Thread(thread, "B");
        Thread c = new Thread(thread, "C");
        a.start();
        b.start();
        c.start();
    }

    /**
     * 总结
     * 在线程中控制的效果不好，
     * 1. 如果先等待，后唤醒，10次循环只执行了1次，也就是第一次循环，原因是进入到等待状态，后续的代码无法执行
     * 2. 如果先唤醒，后等待，10次循环可全部执行，但是执行结束后程序并未结束运行，可以分析出是有锁还处在等待状态
     * 而是应该将控制抽离出来，改为使用控制类去控制每个线程的打印及等待和唤醒。
     */
    static class ABCThread implements Runnable {
        private int number = 1;
        private Thread nextThread;
        private ReentrantLock mLock = new ReentrantLock();
        private Condition mConditionA = mLock.newCondition();
        private Condition mConditionC = mLock.newCondition();
        private Condition mConditionB = mLock.newCondition();

        public void setNextThread(Thread abcThread) {
            nextThread = abcThread;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                modifyNumber();
                checkNumber();
            }
        }

        private void checkNumber() {
            if (number == 1) {

            }
        }

        private void modifyNumber() {
            try {
                mLock.lock();
                if (number == 1) {
                    System.out.println(Thread.currentThread().getName());
                    number = 2;
                    mConditionA.await();
                    mConditionB.signal();
                } else if (number == 2) {
                    System.out.println(Thread.currentThread().getName());
                    number = 3;
                    mConditionB.await();
                    mConditionC.signal();
                } else if (number == 3) {
                    System.out.println(Thread.currentThread().getName());
                    number = 1;
                    mConditionC.await();
                    mConditionA.signal();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mLock.unlock();
            }

        }
    }

    /**
     * 顺序打印 ABC 十次
     * 此类用来控制线程的轮流打印，所以本身不能设定为线程类
     */
    static class ABCAlternate {
        private int number = 1;
        ReentrantLock mLock = new ReentrantLock();
        Condition mConditionA = mLock.newCondition();
        Condition mConditionB = mLock.newCondition();
        Condition mConditionC = mLock.newCondition();

        // 控制 A线程 去打印
        public void printA() {
            try {
                // 加锁
                mLock.lock();
                // 如果当前还未轮到本线程打印，就先等待
                if (number != 1) {
                    try {
                        mConditionA.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 执行本次打印
                System.out.print(Thread.currentThread().getName());
                number = 2;
                // 并唤醒下一线程
                mConditionB.signal();
            } finally {
                // 释放锁
                mLock.unlock();
            }
        }

        // 控制 B线程 去打印
        public void printB() {
            try {
                // 加锁
                mLock.lock();
                // 如果当前还未轮到本线程打印，就先等待
                if (number != 2) {
                    try {
                        mConditionB.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 执行本次打印
                System.out.print(Thread.currentThread().getName());
                number = 3;
                // 并唤醒下一线程
                mConditionC.signal();
            } finally {
                // 释放锁
                mLock.unlock();
            }
        }

        // 控制 C线程 去打印
        public void printC() {
            try {
                // 加锁
                mLock.lock();
                // 如果当前还未轮到本线程打印，就先等待
                if (number != 3) {
                    try {
                        mConditionC.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 执行本次打印
                System.out.print(Thread.currentThread().getName());
                number = 1;
                // 并唤醒下一线程
                mConditionA.signal();
            } finally {
                // 释放锁
                mLock.unlock();
            }
        }
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
