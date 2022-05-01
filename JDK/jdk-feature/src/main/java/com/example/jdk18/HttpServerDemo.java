package com.example.jdk18;


import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class HttpServerDemo {

    public static void main(String[] args) throws IOException {
        var handler = SimpleFileServer.createFileHandler(Path.of("/some/path"));
        var server = HttpServer.create(new InetSocketAddress(8080),
     10, "/store/", handler);
        server.createContext("/browse/", handler);
        server.start();
    }
}
