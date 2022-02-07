package com.javase.io.bio;

import java.net.Socket;

public class BioHandler implements Runnable{

    private Socket socket;

    public BioHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println(socket.toString());
    }
}
