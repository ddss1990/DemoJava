package com.dss.java.tests.juc;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * FileName: TestForkJoin
 * Author: Chris
 * Date: 2018/11/8 10:37
 * Description: 测试拆分任务与合并结果
 * 想要实现ForkJoin框架，需要继承 RecursiveTask 或 RecursiveAction
 */
public class TestForkJoin {
    /**
     * 程序启动
     */
    public void start() {
        // 比较三种方式的计算时间
        // 100万
        compareTime(0L, 1000000L);
        // 一亿
        compareTime(0L, 100000000L);
        // 100亿
        compareTime(0L, 10000000000L);
    }

    /**
     * 比较三种方式的计算时间
     *
     * @param start 开始的数
     * @param end   结束的数
     */
    private void compareTime(long start, long end) {
        /**
         * From 0 to 1000000 is 500000500000 使用ForkJoin框架共用时 8 ms
         * From 0 to 1000000 is 500000500000 使用正常for共用时 4 ms
         * From 0 to 1000000 is 500000500000 使用java8新特性共用时 103 ms
         * From 0 to 100000000 is 5000000050000000 使用ForkJoin框架共用时 65 ms
         * From 0 to 100000000 is 5000000050000000 使用正常for共用时 230 ms
         * From 0 to 100000000 is 5000000050000000 使用java8新特性共用时 382 ms
         * From 0 to 10000000000 is -5340232216128654848 使用ForkJoin框架共用时 5512 ms
         * From 0 to 10000000000 is -5340232216128654848 使用正常for共用时 10762 ms
         * From 0 to 10000000000 is -5340232216128654848 使用java8新特性共用时 35731 ms
         */
        // 使用ForkJoin框架计算的时间
        testForkJoin(start, end);
        // 使用正常for循环的时间
        testNormalFor(start, end);
        // 使用java8新特性的时间
        testJava8(start, end);
    }

    private void testJava8(long start, long end) {
        Instant begin_time = Instant.now();
        long sum = LongStream.rangeClosed(start, end).parallel().reduce(0L, Long::sum);
        Instant end_time = Instant.now();
        System.out.println("From " + start + " to " + end + " is " + sum + " 使用ForkJoin框架共用时 " + Duration.between(begin_time, end_time).toMillis() + " ms");
    }

    /**
     * 计算正常for循环的时间
     *
     * @param start
     * @param end
     */
    private void testNormalFor(long start, long end) {
        Instant begin_time = Instant.now();
        long sum = 0;
        for (long i = start; i <= end; i++) {
            sum += i;
        }
        Instant end_time = Instant.now();
        System.out.println("From " + start + " to " + end + " is " + sum + " 使用ForkJoin框架共用时 " + Duration.between(begin_time, end_time).toMillis() + " ms");
    }

    /**
     * 使用ForkJoin框架计算的时间
     *
     * @param start 开始的数
     * @param end   结束的数
     */
    private void testForkJoin(long start, long end) {
        Instant begin_time = Instant.now();
        // ForkJoin 框架需要依赖 ForkJoinPool 去执行
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> calculator = new ForkJoinSumCalculator(start, end);
        // 执行
        Long sum = pool.invoke(calculator);
        Instant end_time = Instant.now();
        System.out.println("From " + start + " to " + end + " is " + sum + " 使用ForkJoin框架共用时 " + Duration.between(begin_time, end_time).toMillis() + " ms");
    }

    class ForkJoinSumCalculator extends RecursiveTask<Long> {
        private static final long serialVersionUID = -259195479995561737L;
        // 开始值和结束值
        private long start;
        private long end;

        // 临界值
        private long threshold = 10000L;

        public ForkJoinSumCalculator() {

        }

        public ForkJoinSumCalculator(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public void setThreshold(long threshold) {
            this.threshold = threshold;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        /**
         * 对任务进行拆分与合并
         *
         * @return
         */
        @Override
        protected Long compute() {
            // 计算当前其实值与结束值之间的差值
            long length = end - start;
            // 比较是否达到临界值
            if (length <= threshold) {
                // 小于临界值，不再进行拆分，进行计算处理
                long sum = 0;
                for (long i = start; i <= end; i++) {
                    sum += i;
                }
                return sum;
            } else {
                // 没有达到临界值，进行拆分处理
                long mid = (end + start) / 2;
                // 向左拆分
                ForkJoinSumCalculator left = new ForkJoinSumCalculator(start, mid);
                left.fork();
                // 向右拆分
                ForkJoinSumCalculator right = new ForkJoinSumCalculator(mid + 1, end);
                right.fork();

                // 将最终结果合并返回
                return left.join() + right.join();
            }
        }
    }
}
