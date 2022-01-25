package com.jvm.practice.generate;


import org.objectweb.asm.ClassWriter;

public class MyClassLoader extends ClassLoader {

    @Override
    protected Class findClass(String name)throws ClassNotFoundException {
        if (name.endsWith("_Stub")) {
            ClassWriter cw = new ClassWriter(0);

            byte[] b = cw.toByteArray();
            return defineClass(name, b, 0, b.length);
        }
        return super.findClass(name);
    }

    public Class defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
