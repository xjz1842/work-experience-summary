package com.javase.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServer {

    public static void main(String[] args) {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            // 设置非阻塞
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));

            while (true) {
                SocketChannel socketChannel =
                        serverSocketChannel.accept();

                if (socketChannel != null) {
                    //do something with socketChannel..
                    System.out.println(socketChannel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
