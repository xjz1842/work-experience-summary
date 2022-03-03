package com.javase.jni;

/**
 *
 *  .1. 生成TestJNI.h的头文件
 *    cd \src\main\java
 *    javah -classpath . -jni -jni com.javase.jni.TestJNI
 *  2. 编译
 *  gcc -Wall -fPIC -c TestJNI.c
 * -I ./ \
 * -I /home/mazhi/workspace/jdk1.8.0_192/include/linux/ \
 * -I /home/mazhi/workspace/jdk1.8.0_192/include/
 *
 * 3. 生成动态链接库
 *  gcc -Wall -rdynamic -shared -o libtestNative.so TestJNI.o
 */
public class TestJNI {

    static {
        System.loadLibrary("testNative.so");
    }

    public static int getResult() {
        return 2;
    }

    public static native int get();

    public static void main(String[] args) {
        TestJNI.get();
    }

}
