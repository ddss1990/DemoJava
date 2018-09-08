package com.vestel.dss.tests;

import com.vestel.dss.bean.Cat;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

/**
 * FileName: TestSet
 * Author: Chris
 * Date: 2018/9/4 15:54
 * Description: Test Set
 */
public class TestSet {

    public static void main(String[] args) {
        HashSet<Object> set = new HashSet<>();
        set.add(new Cat("Tom", 4));
        set.add(new Cat("Tom", 4));
        System.out.println("set.length = " + set.size() + " ,set = " + set);

        List list = new ArrayList<Integer>();
        List synchronizedList = Collections.synchronizedList(list);
        Object[] objects = list.toArray();
    }

    public <T> void testFan(T t) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait();
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private synchronized void testSync() {
//        Class
    }

    @Test
    @SuppressWarnings("")
    public void testEnum() throws IOException {
        Week[] values = Week.values();
        for (Week week : values) {
            System.out.println("week = " + week);
        }
        System.out.println("values = " + Arrays.toString(values));

        File file = new File("");
        System.setOut(new PrintStream(new FileOutputStream("")));
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(""));
        Object obj = null;
        //RandomAccessFile
        objectOutputStream.writeObject(obj);
        FileReader reader = null;
        try {
            reader = null;
            int read = reader.read(new char[10]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

enum Week {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
}
