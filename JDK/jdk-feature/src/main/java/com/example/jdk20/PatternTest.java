package com.example.jdk20;


/**
 * 模式匹配
 *
 * @author xjz18
 * @version V1.0
 * @date 2023/4/12 19:22
 */
public class PatternTest {

    public static void main(String[] args) {
        System.out.println(formatterPatternSwitch("1"));
        System.out.println(formatterPatternSwitch(1));
    }

    static String formatterPatternSwitch(Object obj) {
        return null;
//        return switch (obj) {
//            case Integer i -> String.format("int %d", i);
//            case Long l    -> String.format("long %d", l);
//            case Double d  -> String.format("double %f", d);
//            case String s  -> String.format("String %s", s);
//            default        -> obj.toString();
//        };
    }
}
