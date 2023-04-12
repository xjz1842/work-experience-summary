package com.example.jdk19;

/**
 * 模式匹配
 *
 * @author xjz18
 * @version V1.0
 * @date 2023/4/12 17:48
 */
public class PatternTest {

    public static void main(String[] args) {
        formatter(1);
        formatter("2");
    }
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
}
