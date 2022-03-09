package com.concurrent.lockfree;

public class VolatileTest {

    volatile Integer str = 2;

    public static void main(String[] args) {
        VolatileTest tearDown = new VolatileTest();
        tearDown.str = 288;
        System.out.println(tearDown.str);

        new Thread(()->{
            System.out.println(tearDown.str++);
        }).start();
        System.out.println(tearDown.str++);;
    }
}
