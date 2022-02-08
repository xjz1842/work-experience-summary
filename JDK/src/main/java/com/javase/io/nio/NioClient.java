package com.javase.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open()){
            System.out.println(socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999)));
            String newData = "New String to write to file..." + System.currentTimeMillis();

            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.clear();
            buf.put(newData.getBytes());
            //从头开始读取
            buf.flip();
            socketChannel.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
