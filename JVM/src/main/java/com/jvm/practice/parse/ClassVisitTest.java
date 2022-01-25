package com.jvm.practice.parse;


import org.objectweb.asm.ClassReader;
import org.openjdk.jol.info.ClassLayout;

import java.io.IOException;

public class ClassVisitTest {

    public static void main(String[] args) throws IOException {
        ClassPrinter cp = new ClassPrinter();

        ClassReader cr = new ClassReader("java.lang.Runnable");
        cr.accept(cp, 0);

        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
    }
}
