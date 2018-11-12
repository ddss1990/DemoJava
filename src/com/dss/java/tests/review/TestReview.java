package com.dss.java.tests.review;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * FileName: TestReview
 * Author: Chris
 * Date: 2018/11/12 9:33
 * Description: Review
 */
public class TestReview {

    /**
     * 2018年11月12日 09:34:27  review NIO
     */
    private void reviewNIO() throws IOException {
        // 缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ShortBuffer shortBuffer;
        IntBuffer intBuffer;
        LongBuffer longBuffer;
        FloatBuffer floatBuffer;
        DoubleBuffer doubleBuffer;
        // 这个不是缓冲区，不是 NIO 包下的，是 java.lang 包下的
        StringBuffer stringBuffer;

        // 获得管道的三种方式
        // 1
        FileInputStream fis = null;
        FileChannel fileChannel = fis.getChannel();
        // 2
        Path path = Paths.get("");
        FileChannel fileChannel1 = FileChannel.open(path, StandardOpenOption.READ);
        // 3
        SeekableByteChannel seekableByteChannel = Files.newByteChannel(path, StandardOpenOption.READ);

        // 直接缓冲区 - 内存映射文件
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

        while (fileChannel.read(byteBuffer) != -1) {

        }
    }
}
