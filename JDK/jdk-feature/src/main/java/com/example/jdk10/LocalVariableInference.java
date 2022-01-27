package com.example.jdk10;

import java.util.ArrayList;

public class LocalVariableInference {

    public static void main(String[] args) {
        var list = new ArrayList<String>(); // infers ArrayList<String>
        list.add("1");
        list.add("2");
        //lambda 参数表达式
        list.stream().forEach(e->{System.out.println(e);});
    }
}
