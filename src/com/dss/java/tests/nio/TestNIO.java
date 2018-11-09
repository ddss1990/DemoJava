package com.dss.java.tests.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * FileName: TestNIO
 * Author: Chris
 * Date: 2018/11/9 9:59
 * Description: Test NIO
 * NIO - New IO
 */
public class TestNIO {
    @Test
    /**
     * 测试缓冲区 - Buffer
     */
    public void testBuffer() {
        // 通过 allocate() 方法创建一个指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        printBaseMessage(buffer, "Allocate");
        String str = "abcde";
        // 向缓冲区存入数据
        buffer.put(str.getBytes());
        printBaseMessage(buffer, "Put");
        // 切换到读取模式
        buffer.flip();
        printBaseMessage(buffer, "Flip");
        // 从缓冲区读取数据，如果不切换模式，是读取不出来数据的，同时position会继续增加
        byte[] dst = new byte[buffer.limit()];
        buffer.get(dst, 0, 2);
        printBaseMessage(buffer, "Get");
        System.out.println(new String(dst));
        // mark一下，再次读取2个字节的数据
        buffer.mark();
        buffer.get(dst, 2, 2);
        printBaseMessage(buffer, "Mark & Get");
        // reset - 将position恢复到mark时的位置
        buffer.reset();
        printBaseMessage(buffer, "Reset");
        // 判断缓冲区中是否还有数据，有的话，还有多少数据
        boolean remaining = buffer.hasRemaining();
        int remaining1 = buffer.remaining();
        System.out.println("hasRemaining = " + remaining + ", remaining = " + remaining1);
        // 清空缓冲区
        buffer.clear();
        printBaseMessage(buffer, "Clear");
        // 清空缓冲区之后，数据并没有被删除，只是重置了 position 和 limit 的位置，数据处于被遗忘的状态
        buffer.get(dst);

        // 直接缓冲区
        System.out.println(new String(dst));
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer.isDirect());
    }

    /**
     * 打印一些基本信息，包括刚执行的方法和一些基本信息(position, limit, capacity)
     *
     * @param buffer
     * @param msg
     */
    private void printBaseMessage(ByteBuffer buffer, String msg) {
        int position = buffer.position();
        int limit = buffer.limit();
        int capacity = buffer.capacity();
        System.out.println(msg + " [position = " + position + ", limit = " + limit + ", capacity = " + capacity + "]");
    }

    @Test
    /**
     * 测试通道 - Channel
     */
    public void testChannel() throws IOException {
        // 方式1 - 通过 getChannel() 方法获得通道
        FileInputStream fis = new FileInputStream("");
        FileChannel channel = fis.getChannel();
        // 方式2 - 通过 open() 方法获得通道
        Path path = null;
        FileChannel channel1 = FileChannel.open(path);
        // 方式3 - 通过 Files 类的 newByteChannel() 方法获得通道
        SeekableByteChannel seekableByteChannel = Files.newByteChannel(path);
    }

    @Test
    /**
     * 复制文件
     */
    public void testCopyFile() throws IOException {
        String fromPath = "D:\\tmp\\";
        fromPath = "C:\\Users\\Chris\\Pictures\\飞机\\";
        fromPath = "D:\\study\\java\\06 NIO\\";
        String fromFileName = "640.gif";
        fromFileName = "nio.zip";
        String fromFile = fromPath + fromFileName;
        String toPath = "D:\\tmp\\";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String toFileName = sdf.format(new Date());
        String toFileFormat = ".gif";
        toFileFormat = ".zip";
        String toFile = toPath + toFileName + toFileFormat;
        // 非直接缓冲区
        copyFileBuffer(fromFile, toFile);
        // 直接缓冲区
        toFileName = sdf.format(new Date());
        toFile = toPath + toFileName + toFileFormat;
        copyFileDirectBuffer(fromFile, toFile);
    }

    /**
     * 使用直接缓冲区复制文件
     *
     * @param fromFile
     * @param toFile
     */
    private void copyFileDirectBuffer(String fromFile, String toFile) throws IOException {
        Instant begin = Instant.now();
        // 通过open()的方式创建通道
        Path inPath = Paths.get(fromFile);
        FileChannel inChannel = FileChannel.open(inPath, StandardOpenOption.READ);
        Path outPath = Paths.get(toFile);
        /**
         * READ - 读
         * WRITE- 写
         * CREATE - 没有就创建，有就覆盖
         * CREATE_NEW - 没有就创建，有就报错
         */
        FileChannel outChannel = FileChannel.open(outPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        // 直接缓冲区 - 内存映射文件
        MappedByteBuffer inBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        // 对缓冲区进行读写操作
        // 注意数组大小，要设置为
        byte[] dst = new byte[inBuffer.limit()];
        inBuffer.get(dst);
        outBuffer.put(dst);

        // 关闭通道
        outChannel.close();
        inChannel.close();
        Instant end = Instant.now();
        long mill = Duration.between(begin, end).toMillis();
        System.out.println("Copy file by DirectBuffer use  " + mill + " ms");
    }

    /**
     * 使用缓冲区复制文件
     *
     * @param fromFile
     * @param toFile
     */
    private void copyFileBuffer(String fromFile, String toFile) throws IOException {
        Instant begin = Instant.now();
        // 输入输出流
        FileInputStream fis = new FileInputStream(fromFile);
        FileOutputStream fos = new FileOutputStream(toFile);

        // 输入输出通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        // 非直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 复制文件
        while (inChannel.read(buffer) != -1) {
            // 转成读模式
            buffer.flip();
            // 这里方法不能使用 write(buffer, buffer.position())，因为flip()之后 position 变为了0
            outChannel.write(buffer);
            // 清空缓冲区
            buffer.clear();
        }

        // 关闭通道和缓冲区
        outChannel.close();
        inChannel.close();
        fos.close();
        fis.close();
        Instant end = Instant.now();
        long mill = Duration.between(begin, end).toMillis();
        System.out.println("Copy file by Buffer use  " + mill + " ms");
    }
}
