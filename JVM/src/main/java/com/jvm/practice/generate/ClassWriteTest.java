package com.jvm.practice.generate;



import org.objectweb.asm.ClassWriter;
import static org.objectweb.asm.Opcodes.*;


public class ClassWriteTest {


    public static void main(String[] args) throws Exception{

        ClassWriter cw = new ClassWriter(0);

        cw.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "com/jvm/practice/generate/Comparable", null, "java/lang/Object",
                null);

        cw.visitField(ACC_PUBLIC+ACC_STATIC+ACC_FINAL,"LESS","I","LESS",1).visitEnd();

        cw.visitEnd();

        byte[] b = cw.toByteArray();

        MyClassLoader myClassLoader = new MyClassLoader();

        Class c = myClassLoader.defineClass("com.jvm.practice.generate.Comparable", b);

        System.out.println(c.getField("LESS"));

    }
}
