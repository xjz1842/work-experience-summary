package com.example.jdk8.interfaceEx.impl;


import com.example.jdk8.interfaceEx.StudentService;

import java.util.concurrent.locks.LockSupport;

public class StudentServiceImpl implements StudentService {

    public static void main(String[] args) {
        StudentServiceImpl studentService = new StudentServiceImpl();

        studentService.getStudent1();

        LockSupport.park();
    }
}
