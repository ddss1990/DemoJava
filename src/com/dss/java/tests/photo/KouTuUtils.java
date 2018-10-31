package com.dss.java.tests.photo;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FileName: KouTuUtils
 * Author: Chris
 * Date: 2018/10/30 10:15
 * Description: 抠图
 */
public class KouTuUtils {

    public static void main(String[] args) {
        testBegin();
    }

    @Test
    public static void testBegin() {
        Scanner scanner = new Scanner(System.in);
        String link = null;
        System.out.print("输入图片链接:");
//        link = "https://mmbiz.qpic.cn/mmbiz_jpg/3H9Lic9MwzSiaQoGWqcm6hqH5gbomI4QZicepRM52f0ytmydOtggdfaQ6cmFlLiccbo08t6LVOiaC8EribVBuBdDJRLg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1";
        link = scanner.next();
        String outputFormat = null;

        System.out.println("1. jpeg");
        System.out.println("2. webp");
        System.out.println("3. jpg");
        System.out.println("0. gif");
        System.out.print("输入图片格式:");
        //outputFormat = OutputFormat.IMG_JPEG;
//        outputFormat = OutputFormat.IMG_WEBP;
        //Date date = new Date();
        outputFormat = OutputFormat.getImageFormat(scanner.nextInt());
        scanner.nextLine();

        System.out.print("输入新的存储目录(默认：C:\\Users\\Chris\\Pictures\\飞机)：");
        String fileBase = null;
        fileBase = scanner.nextLine();
        if (fileBase == null || "".equals(fileBase))
            fileBase = OutputFormat.IMG_BASE_AIR_PLANE;
        System.out.println("fileBase = " + fileBase);

        // name
        System.out.print("输入保存的文件名：");
        String name = null;
        name = scanner.nextLine();
        if (name == null || "".equals(name)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            name = sdf.format(System.currentTimeMillis());
        }

        String fileName = name + outputFormat;
        System.out.println("fileName = " + fileName);
        String outputFile = fileBase + fileName;
        System.out.println("outputFile = " + outputFile);

        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            //OutputStream outputStream = connection.getOutputStream();
            InputStream is = connection.getInputStream();
//            String header = connection.getHeaderField(6);
//            Map<String, List<String>> map = connection.getHeaderFields();
//            System.out.println(map);
            OutputStream os = new FileOutputStream(outputFile);
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = is.read(data)) != -1) {
                os.write(data, 0, len);
            }
            os.flush();
            os.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    static class OutputFormat {
        private static final String IMG_BASE_AIR_PLANE = "C:\\Users\\Chris\\Pictures\\飞机\\";
        private static final String IMG_JPEG = ".jpeg";
        private static final String IMG_WEBP = ".webp";
        private static final String IMG_JPG = ".jpg";
        private static final String IMG_GIF = ".gif";

        private static final int IMG_INT_JPEG = 1;
        private static final int IMG_INT_WEBP = 2;
        private static final int IMG_INT_JPG = 3;
        private static final int IMG_INT_GIF = 0;

        static Map<Integer, String> IMG_FORMAT;

        static {
            IMG_FORMAT = new HashMap<>();
            IMG_FORMAT.put(IMG_INT_JPEG, IMG_JPEG);
            IMG_FORMAT.put(IMG_INT_WEBP, IMG_WEBP);
            IMG_FORMAT.put(IMG_INT_JPG, IMG_JPG);
            IMG_FORMAT.put(IMG_INT_GIF, IMG_GIF);
        }

        public static String getImageFormat(int format) {
            return IMG_FORMAT.get(format);
        }
    }

}
