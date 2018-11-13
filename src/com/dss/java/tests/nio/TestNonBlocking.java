package com.dss.java.tests.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * FileName: TestNonBlocking
 * Author: Chris
 * Date: 2018/11/13 11:28
 * Description: Non-Blocking 非阻塞式NIO
 */
public class TestNonBlocking {

    public static void main(String[] args) {
        try {
            //testClient();
            // 测试Pipe
            Pipe pipe = Pipe.open();
            SinkThread sinkThread = new SinkThread(pipe);
            SourceThread sourceThread = new SourceThread(pipe);
            new Thread(sinkThread, "SINK").start();
            new Thread(sourceThread, "SOURCE").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端，使用非阻塞式发送数据
     */
    public static void testClient() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now = " + now);

        // 1. 建立通道
        InetAddress addr = InetAddress.getLocalHost();
        SocketAddress remote = new InetSocketAddress(addr, 9999);
        SocketChannel clientChannel = SocketChannel.open(remote);

        // 2. 使用非阻塞式
        clientChannel.configureBlocking(false);

        // 3. 建立缓冲区发送数据
        ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
        // 4. 向缓冲区写入数据
        Scanner scanner = new Scanner(System.in);
        Date date = new Date();
        while (scanner.hasNext()) {
            date.setTime(System.currentTimeMillis());
            String str = scanner.next();
            String msg = date + "\n" + str;
            sendBuffer.put(msg.getBytes());
            // 5. 转换模式
            sendBuffer.flip();
            // 6. 向通道写入缓冲区
            clientChannel.write(sendBuffer);
            // 7. 清空缓冲区
            sendBuffer.clear();
        }
        // 8. 关闭通道
        clientChannel.close();
    }

    @Test
    /**
     * 服务端，使用非阻塞式接受数据
     */
    public void testServer() throws IOException {
        // 1. 服务端管道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 2. 设置非阻塞模式
        serverChannel.configureBlocking(false);
        // 3. 绑定地址和端口号
        InetAddress addr = InetAddress.getLocalHost();
        SocketAddress local = new InetSocketAddress(addr, 9999);
        serverChannel.bind(local);

        // 4. 获取选择器
        Selector selector = Selector.open();
        // 5. 注册选择器，指定监听‘接收事件’
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        //int select = selector.select();
        //System.out.println("select = " + select);

        // 6. 轮询获取选择器上已准备就绪的事件
        while (selector.select() > 0) {
            // 7. 获得当前选择器上所有的注册的“选择键(已就绪的监听事件)”
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //System.out.println("selectionKeys.size() = " + selectionKeys.size());
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            //for (SelectionKey key : selectionKeys) {
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();/*
                System.out.println("isAcceptable = " + key.isAcceptable() +
                        ", isConnectable = " + key.isConnectable() +
                        ", isWritable = " + key.isWritable() +
                        ", isReadable = " + key.isReadable());*/
                // 8. 判断是什么事件
                if (key.isAcceptable()) {
                    // 9. 接收事件，获取客户端连接
                    SocketChannel clientChannel = serverChannel.accept();
                    // 10. 设置非阻塞模式
                    clientChannel.configureBlocking(false);
                    // 11. 将该通道注册到选择器上
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isConnectable()) {

                } else if (key.isReadable()) {
                    // 12. 获取当前选择器上读就绪的通道
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    // 13. 准备缓冲区，接收数据
                    ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
                    int read = 0;
                    // 比较实用 >0 ，不能使用  != -1
                    while ((read = clientChannel.read(receiveBuffer)) > 0) {
                        receiveBuffer.flip();
                        String content = new String(receiveBuffer.array(), 0, read);
                        System.out.println("content = " + content);
                        receiveBuffer.clear();
                    }

                } else if (key.isWritable()) {

                }
                // 处理完选择键之后要移除
                iterator.remove();
            }
        }
    }

    @Test
    /**
     * 使用DatagramChannel进行发送数据
     */
    public void testDatagramClient() throws IOException {
        // 创建通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 设置为非阻塞模式
        datagramChannel.configureBlocking(false);
        // 发送数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress target = new InetSocketAddress(InetAddress.getLocalHost(), 9999);
        datagramChannel.send(buffer, target);
    }

    @Test
    /**
     * 使用DatagramChannel进行接收数据
     */
    public void testDatagramServer() throws IOException {
        // 接收通道
        DatagramChannel receiveChannel = DatagramChannel.open();
        // 奢姿为非阻塞模式
        receiveChannel.configureBlocking(false);

        // 选择器
        Selector selector = Selector.open();
        // 为通道注册选择器
        receiveChannel.register(selector, SelectionKey.OP_READ);

        // 轮询获取选择器上的事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 读事件
                if (key.isReadable()) {
                    // 读数据
                    ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
                    receiveChannel.read(receiveBuffer);
                }
                // 将已处理的事件删除
                iterator.remove();
            }
        }

    }
}

/**
 * 发送Pipe
 */
class SinkThread implements Runnable {
    private Pipe mPipe;

    public SinkThread(Pipe pipe) {
        mPipe = pipe;
    }

    @Override
    /**
     * 发送数据
     */
    public void run() {
        Pipe.SinkChannel sink = mPipe.sink();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            String str = "Hello world";
            buffer.put(str.getBytes());
            buffer.flip();
            System.out.println(Thread.currentThread().getName() + " sink :" + str);
            sink.write(buffer);
            sink.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 接收Pipe
 */
class SourceThread implements Runnable {
    private final Pipe.SourceChannel mSource;
    private Pipe mPipe;

    public SourceThread(Pipe pipe) {
        mPipe = pipe;
        mSource = mPipe.source();
    }

    @Override
    /**
     * 接收数据
     */
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int read = mSource.read(buffer);
            String s = new String(buffer.array(), 0, read);
            System.out.println(Thread.currentThread().getName() + " source : " + s);
            mSource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
