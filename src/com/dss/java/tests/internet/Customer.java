package com.dss.java.tests.internet;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * User: DSS
 * Date: 2018/9/9
 * Time: 22:43
 * Tag: Customer
 */
public class Customer {
    private SendMessenger mSendMessenger = null;
    private ReceiveMessenger mReceiveMessenger = null;
    private String mIpAddress;
    private int mPort;

    public Customer() {
    }

/*    public Customer(String ipAddress, int port) {
        mIpAddress = ipAddress;
        mPort = port;
        mSendMessenger = new SendMessenger(ipAddress, port);
        mReceiveMessenger = new ReceiveMessenger(port);
        new Thread(mSendMessenger).start();
        new Thread(mReceiveMessenger).start();
    }*/

    @Test
    public void testCustomer1() {
        try {
//            Customer customer = new Customer(InetAddress.getLocalHost().getHostAddress(), 8999);
            startSendAndReceive(InetAddress.getLocalHost().getHostAddress(), 8999);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void startSendAndReceive(String ipAddress, int port) {
        SendMessenger sendMessenger = new SendMessenger(ipAddress, port);
        ReceiveMessenger receiveMessenger = new ReceiveMessenger(port);
        new Thread(sendMessenger).start();
        new Thread(receiveMessenger).start();
    }

    @Test
    public void testCustomer2() {
        try {
//            Customer customer = new Customer(InetAddress.getLocalHost().getHostAddress(), 9999);
            startSendAndReceive(InetAddress.getLocalHost().getHostAddress(), 9999);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}

class SendMessenger implements Runnable {
    private String mIpAddress;
    private int mPort;

    public SendMessenger(String ipAddress, int port) {
        mIpAddress = ipAddress;
        mPort = port;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        InputStream in = null;
        Socket socket = null;
        try {
            socket = new Socket(mIpAddress, mPort);
            outputStream = socket.getOutputStream();
            in = System.in;
            byte[] bytes = new byte[1024];
            int len;
            while (true) {
                while ((len = in.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ReceiveMessenger implements Runnable {
    public static int PORT = 8999;
    public static String IP_ADDRESS = "";

    private int mPort;

    public ReceiveMessenger(int port) {
        mPort = port;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        Socket socket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(mPort);
            while (true) {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                byte[] data = new byte[1024];
                int len;
                while ((len = inputStream.read(data)) != -1) {
                    String s = new String(data, 0, len);
                    System.out.println("s = " + s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
