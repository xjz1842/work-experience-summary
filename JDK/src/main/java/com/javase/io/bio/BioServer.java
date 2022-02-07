package com.javase.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {

    public static void main(String[] args) {
        int port = 9009;

        try (ServerSocket serverSocket =  new ServerSocket(port);){

           while (true){
               //如果没有客户端连接则阻塞
              Socket socket =  serverSocket.accept();
              new Thread(new BioHandler(socket)).start();
           }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
