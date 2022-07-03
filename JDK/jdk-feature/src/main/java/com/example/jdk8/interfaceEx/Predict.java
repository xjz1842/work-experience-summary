package com.example.jdk8.interfaceEx;

import java.util.function.Predicate;

public class Predict {

    public static void main(String[] args) {
        Predicate predicate =  getvalidate();
        predicate.test("");
        predicate.test("");
    }

    private static Predicate getvalidate(){
        Integer i = 10;
        System.out.println("===");
        return (t)->{
            System.out.println(i);
            return true;
        };
    }
}
