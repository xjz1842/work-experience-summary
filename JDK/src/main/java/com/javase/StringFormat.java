package com.javase;

import java.text.MessageFormat;

public class StringFormat {


    public static void main(String[] args) {
        String domain = "www.baidu.com";
        int iVisit = 0;
        System.out.println(MessageFormat.format("该域名{0}被访问了 {1} 次.", domain , iVisit));
    }
}
