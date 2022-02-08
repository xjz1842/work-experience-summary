package com.javase.io.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ScatterReadDemo {

    public static void main(String[] args)throws IOException {
        ByteBuffer header = ByteBuffer.allocate(12);
        ByteBuffer body   = ByteBuffer.allocate(1024);

        ByteBuffer[] bufferArray = { header, body };
        RandomAccessFile aFile = new RandomAccessFile("Cnio-data.txt", "rw");

        FileChannel fileChannel = aFile.getChannel();
        fileChannel.read(bufferArray);
        //开始读取
        header.flip();
        body.flip();
        for (int i = 0; i < bufferArray.length; i++) {
            System.out.println("====="+i);
            while (bufferArray[i].hasRemaining()){
                System.out.print((char)bufferArray[i].get());
            }
            System.out.println();
        }
    }
}
