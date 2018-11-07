package com.dss.java.tests.juc;

import java.util.concurrent.CountDownLatch;

/**
 * FileName: Test8Lock
 * Author: Chris
 * Date: 2018/11/7 15:57
 * Description: Test 8 Lock
 */
public class Test8Lock {
    CountDownLatch mLatch;

    public void begin() throws InterruptedException {
        mLatch = new CountDownLatch(2);
        System.out.print("1. 两个同步方法，两个线程：");
        function1();
        mLatch.await();
        System.out.println();

        System.out.print("2. 两个同步方法，方法1有延时，两个线程：");
        mLatch = new CountDownLatch(2);
        function2();
        mLatch.await();
        System.out.println();

        System.out.print("3. 两个同步方法，一个非同步方法，三个线程 ：");
        mLatch = new CountDownLatch(3);
        function3();
        mLatch.await();
        System.out.println();

        System.out.print("4. 两个同步方法，两个对象，两个线程 ：");
        mLatch = new CountDownLatch(2);
        function4();
        mLatch.await();
        System.out.println();

        System.out.print("5. 一个静态同步方法，一个同步方法，两个线程 ：");
        mLatch = new CountDownLatch(2);
        function5();
        mLatch.await();
        System.out.println();

        System.out.print("6. 两个静态同步方法，两个线程 ：");
        mLatch = new CountDownLatch(2);
        function6();
        mLatch.await();
        System.out.println();

        System.out.print("7. 一个静态同步方法，一个非静态同步方法，两个对象，两个线程 ：");
        mLatch = new CountDownLatch(2);
        function7();
        mLatch.await();
        System.out.println();

        System.out.print("8. 两个静态同步方法，两个对象，两个线程 ：");
        mLatch = new CountDownLatch(2);
        function8();
        mLatch.await();
    }

    /**
     * 两个静态同步方法，两个对象，两个线程
     */
    private void function8() {
        Test8Lock test8Lock = new Test8Lock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOneStatic();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                test8Lock.getTwoStatic();
                mLatch.countDown();
            }
        }).start();
    }

    /**
     * 一个静态同步方法，一个非静态同步方法，两个对象，两个线程
     */
    private void function7() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOneStatic();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTwo();
                mLatch.countDown();
            }
        }).start();
    }

    /**
     * 两个静态同步方法，两个线程
     */
    private void function6() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOneStatic();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTwoStatic();
                mLatch.countDown();
            }
        }).start();
    }

    /**
     * 一个静态同步方法，一个同步方法，两个线程
     */
    private void function5() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOneStatic();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTwo();
                mLatch.countDown();
            }
        }).start();
    }

    /**
     * 两个同步方法，两个对象，两个线程
     */
    private void function4() {
        Test8Lock test8Lock = new Test8Lock();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOne();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                test8Lock.getTwo();
                mLatch.countDown();
            }
        }).start();
    }

    /**
     * 两个同步方法，一个非同步方法，三个线程
     */
    private void function3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOne();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTwo();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getThree();
                mLatch.countDown();
            }
        }).start();
    }

    /**
     * 两个同步方法，方法1有延时，两个线程
     */
    private void function2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOne();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTwo();
                mLatch.countDown();
            }
        }).start();
    }

    /**
     * 两个同步方法，两个线程，没有延时
     */
    private void function1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getOneNoSleep();
                mLatch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTwo();
                mLatch.countDown();
            }
        }).start();
    }

    public synchronized void getOne() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(" One ");
    }

    public synchronized void getOneNoSleep() {
        System.out.print(" One ");
    }

    public static synchronized void getOneStatic() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(" One ");
    }

    public synchronized void getTwo() {
        System.out.print(" Two ");
    }

    public synchronized void getTwoStatic() {
        System.out.print(" Two ");
    }

    public void getThree() {
        System.out.print(" Three ");
    }

}
