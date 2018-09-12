package com.dss.java.tests.internet;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * FileName: TestUDP
 * Author: Chris
 * Date: 2018/9/12 16:46
 * Description: Test UDP
 */
public class TestUDP {
    public static void main(String[] args) {

    }

    /**
     * 发送数据报
     */
    @Test
    public void send() {
        DatagramSocket datagramSocket = null;
        try {
            // 先创建一个UDP的socket，不需要指明IP地址和端口号，因为要发送的地址会在数据报中指明
            datagramSocket = new DatagramSocket();
            // 再创建要发送的数据报
            // 要发送的数据
            //byte[] bytes = "Hello Wrold".getBytes();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 1025; i++) {
                sb.append(1);
            }
            byte[] bytes = sb.toString().getBytes();
            System.out.println("bytes.length = " + bytes.length);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length, InetAddress.getLocalHost(), 8999);

            // 发送数据报
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    /**
     * 接收数据报
     * bytes : 接收的数据报中的参数，bytes.length : 长度等于声明的字节数组长度
     * data : 通过数据报调用方法 getData() 所得 data.length :  长度等于声明的字节数组的长度
     * length : 接收到的字节数组的有效数据长度，即如果发送的数据长度小于字节数组的长度，就是数据的实际长度；
     * 如果发送的数据长度大于声明的字节数组的长度，就是数组的长度
     */
    @Test
    public void receive() {
        DatagramSocket datagramSocket = null;
        try {
            // 先创建一个UDP的socket，只需要指明端口号
            datagramSocket = new DatagramSocket(8999);
            byte[] bytes = new byte[1024];
            // 创建用于接收的数据报
            DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);

            // 接收数据报
            datagramSocket.receive(datagramPacket);

            InetAddress fromAddress = datagramPacket.getAddress();
            System.out.println("fromAddress = " + fromAddress);
            byte[] data = datagramPacket.getData();
//        System.out.println("(bytes == data) = " + (bytes == data));
            String s_bytes = new String(bytes, 0, bytes.length);
            String s_data = new String(data, 0, data.length);
            System.out.println("s_bytes = " + s_bytes);
            System.out.println("s_data = " + s_data);
            System.out.println("data.length = " + data.length + ", bytes.length = " + bytes.length + ", packet.length = " + datagramPacket.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }


    }
}
