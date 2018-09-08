package com.vestel.dss.tests;

/**
 * FileName: TestThrad
 * Author: Chris
 * Date: 2018/9/6 15:49
 * Description: Test Thread
 */
public class TestThrad {

    public static void main(String[] args) {
        PrintNum num = new PrintNum();
        Thread thread1 = new Thread(num);
        Thread thread2 = new Thread(num);

        thread1.setName("甲");
        thread2.setName("乙");

        thread1.start();
        thread2.start();

        String intern = "".intern();
    }


}

class PrintNum implements Runnable {
    int num = 1;
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                notify();
                if (num <= 100) {
                    System.out.println(Thread.currentThread().getName() + "  num = " + num);
                    num++;
                } else {
                    break;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
