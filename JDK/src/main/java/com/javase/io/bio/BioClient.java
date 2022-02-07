package com.javase.io.bio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BioClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 9009;

        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port));

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
