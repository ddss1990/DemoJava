package com.dss.java.tests.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * FileName: TestBlocking
 * Author: Chris
 * Date: 2018/11/13 10:24
 * Description: Blocking NIO 阻塞式NIO
 */
public class TestBlocking {
    TestNIO nio = new TestNIO();

    @Test
    /**
     * 模拟客户端向服务端发送数据
     * 客户端
     */
    public void testClient() throws IOException {
        // 服务端地址
        SocketAddress remote = new InetSocketAddress(InetAddress.getLocalHost(), 9999);
        // 建立通道
        SocketChannel clientChannel = SocketChannel.open(remote);

        // 缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("Hello Server".getBytes());

        // 将数据写入到通道中，写入通道之前还是需要切换模式：之前的模式是向buffer中写数据，之后是从buffer中读数据到channel中
        // before : position = 12, limit = 1024, capacity = 1024
        // after : position = 0, limit = 12, capacity = 1024
        // nio.printBaseMessage(buffer, "before");
        buffer.flip();
        // nio.printBaseMessage(buffer, "after");
        clientChannel.write(buffer);

        // 避免发生阻塞，应在发送玩数据后，关闭掉输出流
        clientChannel.shutdownOutput();

        // 接收服务器发送的数据
        ByteBuffer receiverBuffer = ByteBuffer.allocate(1024);
        clientChannel.read(receiverBuffer);
        receiverBuffer.flip();
        String s = new String(receiverBuffer.array());
        System.out.println("s = " + s);

        // 关闭通道
        clientChannel.close();
    }

    @Test
    /**
     * 服务端
     * 接收数据
     */
    public void testServer() throws IOException {
        // 服务器通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        InetSocketAddress local = new InetSocketAddress(InetAddress.getLocalHost(), 9999);
        // 绑定端口号
        serverChannel.bind(local);

        // 打开连接客户端的通道
        SocketChannel acceptChannel = serverChannel.accept();

        // 创建缓冲区，将数据读取到缓冲区中
        ByteBuffer dst = ByteBuffer.allocate(1024);
        acceptChannel.read(dst);
        // before : position = 12, limit = 1024, capacity = 1024
        // after : position = 0, limit = 12, capacity = 1024
        // 这里就算不 flip() 也是能读取到数据的
        dst.flip();
        nio.printBaseMessage(dst, "accept buffer");

        String s = new String(dst.array());
        System.out.println("s = " + s);

        // 向客户端发送数据
        ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
        sendBuffer.put("Hello Client".getBytes());
        sendBuffer.flip();
        acceptChannel.write(sendBuffer);

        // 关闭通道
        acceptChannel.close();
        serverChannel.close();
    }

}
