package com.example.jdk8.interfaceEx;

public interface StudentService {

    default void getStudent1() {
        String stu = getStu();
        System.out.println("stu1======>" + stu);
    }

    default void getStudent2() {
        String stu = getStu();
        System.out.println("stu2======>" + stu);
    }

    private String getStu() {
        return "hello world!";
    }


}



