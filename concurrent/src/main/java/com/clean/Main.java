package com.clean;

public class Main {

    public static void main(String[] args) {
        CleaningExample cleaningExample = new CleaningExample();
        cleaningExample = null;
        System.gc();
    }
}
