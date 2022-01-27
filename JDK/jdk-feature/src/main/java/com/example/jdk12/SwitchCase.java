package com.example.jdk12;


public class SwitchCase {

    public static void main(String[] args) {
        howMany(1);
        howMany(2);
        howMany(3);
    }

    static void howMany(int k) {
        switch (k) {
            case 1 -> System.out.println("one");
            case 2 -> System.out.println("two");
            case 3 -> System.out.println("many");
        }
    }
}
