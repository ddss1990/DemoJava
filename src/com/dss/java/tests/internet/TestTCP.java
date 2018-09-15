package com.dss.java.tests.internet;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * User: DSS
 * Date: 2018/9/9
 * Time: 22:01
 * Tag: Test Tcp
 */
public class TestTCP {
    public static void main(String[] args) {
        ServerThread server = new ServerThread();
        new Thread(server).start();
        ClientThread client = new ClientThread();
        new Thread(client).start();
    }

    @Test
    public void test() {
        try {
            // 得到是本机IP，并非172.0.0.1
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("localHost = " + localHost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    /**
     *  客户端
     */
    @Test
    public void client() {
        Socket socket = null;
        OutputStream outputStream = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 8999); // 选择服务器对应的IP地址和端口号，建立连接
            outputStream = socket.getOutputStream();
            outputStream.write("这里是Client".getBytes());
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
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     *  服务端
     */
    @Test
    public void server() {
        ServerSocket serverSocket = null;
        InputStream inputStream = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(8999);// 这里应该是相当于开启服务器
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
            byte[] data = new byte[1024];
            int len;
            while ((len = inputStream.read(data)) != -1) {
                String s = new String(data, 0, len);
                System.out.println("s = " + s);
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

class ServerThread implements Runnable{

    @Override
    public void run() {
        InputStream inputStream = null;
        Socket socket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8999);
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

class ClientThread implements Runnable{
    @Override
    public void run() {
        OutputStream outputStream = null;
        InputStream in = null;
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 8999);
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

