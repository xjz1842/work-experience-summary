package com.example.jdk8.interfaceEx.date;

import java.time.LocalTime;

public class DateTest {

    public static void main(String[] args) {
        LocalTime time = LocalTime.MAX;

        System.out.println(time.getSecond());
        System.out.println(time.getMinute());
        System.out.println(time.getHour());
        System.out.println(getCronByTime(time));
    }

    private static String getCronByTime(LocalTime time) {
        String cronFormet = "%s %s %s ? * *";
        return String.format(cronFormet, time.getSecond(),time.getMinute(), time.getHour());
    }

}
