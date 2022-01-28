package com.javase.reference;

public class Weak {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("test");
        System.gc();
        System.gc();
        System.out.println(threadLocal.get());
    }
}
