package com.example.jdk17;

/**
 * // Old code
 * if (o instanceof String) {
 *     String s = (String)o;
 *     ... use s ...
 * }
 *
 * // New code
 * if (o instanceof String s) {
 *     ... use s ...
 * }
 */
public class Switch {

    static String formatter(Object o) {
        String formatted = "unknown";
        if (o instanceof Integer i) {
            formatted = String.format("int %d", i);
        } else if (o instanceof Long l) {
            formatted = String.format("long %d", l);
        } else if (o instanceof Double d) {
            formatted = String.format("double %f", d);
        } else if (o instanceof String s) {
            formatted = String.format("String %s", s);
        }
        return formatted;
    }

    public static void main(String[] args) {
        System.out.println(formatter(1));
    }
}
