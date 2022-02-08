package com.javase.io.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

public class AsynchronousFileChannelDemo {

    public static void main(String[] args) {
        Path path = Paths.get("nio-data.txt");

//        // example 1 sync read
//        syncRead(path);
//
//        // example 2 : async callback
//        asyncRead(path);

        // example3 wrtie
//        syncWrite(path);

        //example write callback
        asyncWrite(path);
    }

    private static void asyncWrite(Path path) {
        try (AsynchronousFileChannel fileChannel =
                     AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            buffer.put("data".getBytes());
            buffer.flip();

            fileChannel.write(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println("bytes written: " + result);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("Write failed");
                    exc.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void syncWrite(Path path) {
        try (AsynchronousFileChannel fileChannel =
                     AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            buffer.put("test data".getBytes());
            buffer.flip();

            Future<Integer> operation = fileChannel.write(buffer, position);
            buffer.clear();

            while (!operation.isDone()) {
            }

            System.out.println("Write done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void syncRead(Path path){
        // example 1  sync wait for done
        try (AsynchronousFileChannel fileChannel =
                     AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            Future<Integer> operation = fileChannel.read(buffer, position);

            while (!operation.isDone()) {
            }
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            System.out.println(new String(data));
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void asyncRead(Path path){
        try (AsynchronousFileChannel fileChannel =
                     AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            long position = 0;

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            fileChannel.read(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println("result = " + result);

                    attachment.flip();
                    byte[] data = new byte[attachment.limit()];
                    attachment.get(data);
                    System.out.println(new String(data));
                    attachment.clear();
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}