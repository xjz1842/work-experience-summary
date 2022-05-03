package com.jvm.practice.thread;

public class ThreadDemo {

    public static void main(String[] args) {
        //创建线程
        Thread thread = new Thread(() -> System.out.println("thread"));

        ThreadEx thread1 = new ThreadEx();

        //启动
        thread.start();
        thread1.start();
    }
}
