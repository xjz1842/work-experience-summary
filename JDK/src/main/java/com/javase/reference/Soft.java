package com.javase.reference;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 *  -Xmx=10m -Xms=10m
 */
public class Soft {

    public static class User {
        public  User(){}

        private String name;

        private int age;
    }

    // 1 MB
    private  static  byte[] _1Mb = new byte[1024 * 1024];

    public static void main(String[] args) {
        User user = new User();
        SoftReference softReference = new SoftReference(user);
        System.out.println(softReference.get());

        //手动置空
        user = null;
        List<byte[]> list = new ArrayList<>();
        while (true){
            System.out.println(softReference.get());
            list.add(new byte[1024 * 1024]);
        }
    }
}
