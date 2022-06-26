package com.example.jdk8.interfaceEx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Test
{
    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();
        list.add("2");

        print((t) ->
            System.out.println(list)
        );

        System.out.println( "11111111111111111111111111111111111111111122222222222222222222222222222222222222222222222222222233333333333333333333333333333333333333333333333333333333344444444444444444444444444444444444444444444444444444444444455555555555555555555555555555555555555555555555555555555566666666666666666666666666666666666666666666666666666666666666666666666777777777777777777777777777777777777777777777777777777777777777777777777777777777777888888888888888888888888888888888888888888888888888888888888888888888899999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999额".length());
        Set<String> set = new HashSet<>();
        set.add("123");
        set.remove("123");
        System.out.println(set);

        String s = "1";
        String s1 = "1";
        String s4 = s + s1;
        String s5 = "11";
        System.out.println(s4 == s5);
        System.out.println(s == s1);
    }

     static void print(Consumer<List<String>> consumer){
         consumer.accept(null);
     }
}


