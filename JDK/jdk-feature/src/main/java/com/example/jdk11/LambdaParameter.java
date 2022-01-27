package com.example.jdk11;

import java.util.ArrayList;

public class LambdaParameter {

    public static void main(String[] args) {
        var list = new ArrayList<String>(); // infers ArrayList<String>
        list.add("1");
        list.add("2");
        //lambda 参数表达式
        list.stream().forEach((var e)->{System.out.println(e);});
    }
}
