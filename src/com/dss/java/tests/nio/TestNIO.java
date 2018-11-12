package com.dss.java.tests.nio;

import org.junit.Test;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.SortedMap;

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
        //copyFileBuffer(fromFile, toFile);
        // 直接缓冲区
        toFileName = sdf.format(new Date());
        toFile = toPath + toFileName + toFileFormat;
        //copyFileDirectBuffer(fromFile, toFile);
        // 通道之间的数据传输
        copyFileDirectBuffer2(fromFile, toFile);
    }

    /**
     * 通道之间的数据传输
     * 实测效果更慢了
     * size = 1111244363
     * Copy file by Buffer use  24652 ms
     * Copy file by DirectBuffer use  27946 ms
     *
     * @param fromFile
     * @param toFile
     */
    private void copyFileDirectBuffer2(String fromFile, String toFile) throws IOException {
        Instant begin = Instant.now();
        System.out.println("fromFile = " + fromFile + ", toFile = " + toFile);
        Path fromPath = Paths.get(fromFile);
        FileChannel inChannel = FileChannel.open(fromPath, StandardOpenOption.READ);
        Path toPath = Paths.get(toFile);
        FileChannel outChannel = FileChannel.open(toPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

        // 通道间直接数据传输 transferTo 和 transferFrom 二选一即可
        long transfer = inChannel.transferTo(0, inChannel.size(), outChannel);
        //long transfer = outChannel.transferFrom(inChannel, 0, inChannel.size());
        System.out.println("transfer = " + transfer);

        // 关闭通道
        outChannel.close();
        inChannel.close();

        Instant end = Instant.now();
        long mill = Duration.between(begin, end).toMillis();
        System.out.println("Copy file by DirectBuffer use  " + mill + " ms");
    }

    /**
     * 使用直接缓冲区复制文件
     *
     * @param fromFile
     * @param toFile
     */
    private void copyFileDirectBuffer(String fromFile, String toFile) throws IOException {
        System.out.println("fromFile = " + fromFile + ", toFile = " + toFile);
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
        FileChannel outChannel = FileChannel.open(outPath, StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

        // 直接缓冲区 - 内存映射文件
        System.out.println("inChannel.size = " + inChannel.size());
        MappedByteBuffer inBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        // OutOfMemoryError: Map failed  可能是size太大了，目前是超过1个G - 1111244363
        /**
         * API 中显示不要超过Integer.MAX_VALUE ，但是从数据来看，并没有超过呀
         * Integer.MAX_VALUE - 0x7fffffff - 2147483647
         * 2147483647
         * 1111244363
         */

        MappedByteBuffer outBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        // 对缓冲区进行读写操作
        // 注意数组大小，要设置为直接缓冲区的实际大小
        byte[] dst = new byte[inBuffer.limit()];
        inBuffer.get(dst);
        outBuffer.put(dst);

        // 关闭通道
        inChannel.close();
        outChannel.close();
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

    /**
     * 分散(Scatter) 和 获取(Gather)
     */
    @Test
    public void testScatterGather() throws IOException {
        // 获取读通道
        RandomAccessFile file = new RandomAccessFile("src/c3p0-config.xml", "r");
        FileChannel inChannel = file.getChannel();
        // 创建缓冲区数组
        ByteBuffer buffer1 = ByteBuffer.allocate(100);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
        ByteBuffer[] buffers = {buffer1, buffer2};

        // 将数据写入到缓冲区
        long read = inChannel.read(buffers);
        System.out.println("read = " + read);

        RandomAccessFile outFile = new RandomAccessFile("c3p0-config.xml.bak", "rw");
        FileChannel outChannel = outFile.getChannel();
        /**
         * 这是只有一个缓冲区的情况，使用循环去读写
         while (inChannel.read(buffer1) != -1) {
         buffer1.flip();
         outChannel.write(buffer1);
         buffer1.clear();
         }*/

        for (ByteBuffer buffer : buffers) {
            // 切换到读取缓冲区数据模式
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, buffer.limit()));
            System.out.println("------------------------");
        }

        // 将缓冲区的数据写入到文件中
        long write = outChannel.write(buffers);
        outChannel.close();
        inChannel.close();
        outFile.close();
        file.close();
    }

    @Test
    /**
     * 字符集 ： 编码与解码
     */
    public void testCharSet() throws CharacterCodingException {
        // 可用的字符集有 170 种
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        System.out.println("charsets.size() = " + charsets.size());
//        System.out.println("charsets = " + charsets);

        // 获得指定的字符集
        Charset gbk = Charset.forName("gbk");
        // 获得字符集对应的编码器和解码器
        // 编码器
        CharsetEncoder gbkEncoder = gbk.newEncoder();
        // 解码器
        CharsetDecoder gbkDecoder = gbk.newDecoder();

        String s = "Hello world!";
        s = "厉害了";

        // 编码
        // 通过字符集直接编码
        ByteBuffer gbkEncode = gbk.encode(s);
        printBaseMessage(gbkEncode, "通过字符集GBK进行编码");
        // 通过字符集获得的编码器进行编码，需要用到字符缓冲区
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put(s);
        // 将数据读取到缓冲区之后要切换模式
        charBuffer.flip();
        ByteBuffer encodeByteBuffer = gbkEncoder.encode(charBuffer);
        printBaseMessage(encodeByteBuffer, "通过字符集获得的编码器进行编码");

        // 解码
        // 使用同一字符集进行解码
        // 直接使用字符集进行解码，解的是通过字符集进行编码的东西
        CharBuffer decode1 = gbk.decode(gbkEncode);
        System.out.println("decode1 = " + decode1);
        // 直接使用字符集进行解码，解的是通过编码器进行编码的东西
        CharBuffer decode2 = gbkDecoder.decode(encodeByteBuffer);
        System.out.println("decode2 = " + decode2);

        // 使用不同字符集进行解码
        Charset utf = Charset.forName("utf-8");
        CharsetDecoder utfDecoder = utf.newDecoder();
        gbkEncode.flip();
        CharBuffer utf_decode1 = utf.decode(gbkEncode);
        System.out.println("utf_decode1 = " + utf_decode1);
        encodeByteBuffer.flip();
        CharBuffer gbk_decode2 = gbkDecoder.decode(encodeByteBuffer);
        System.out.println("gbk_decode2 = " + gbk_decode2);
        encodeByteBuffer.flip();
        System.out.println("encodeByteBuffer = " + encodeByteBuffer.hasRemaining());
        printBaseMessage(encodeByteBuffer, "flip");
        // 这里使用 utfDecoder 就会报错，使用 gbkDecoder 就没问题
        CharBuffer utf_decode3 = utfDecoder.decode(encodeByteBuffer);
        System.out.println("utf_decode3 = " + utf_decode3);
    }
}
