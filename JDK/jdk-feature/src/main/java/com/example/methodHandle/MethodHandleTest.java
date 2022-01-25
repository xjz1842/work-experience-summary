package com.example.methodHandle;

import java.io.StringWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import static java.lang.invoke.MethodType.methodType;

public class MethodHandleTest {

    private static void bar(Object o) {
//        new Exception().printStackTrace();
    }

    public static MethodHandles.Lookup lookup() {
        return MethodHandles.lookup();
    }

    public static void main(String[] args) throws Throwable {
        // 获取方法句柄的不同方式
        MethodHandles.Lookup l = MethodHandleTest.lookup();
        // 具备Foo类的访问权限
        Method m = MethodHandleTest.class.getDeclaredMethod("bar", Object.class);

        MethodHandle mh0 = l.unreflect(m);

        mh0.invokeExact((Object) "a");

        MethodType t = methodType(void.class, Object.class);

        MethodHandle mh1 = l.findStatic(MethodHandleTest.class, "bar", t);

        //BindTo
        StringWriter swr = new StringWriter();
        MethodHandle swWrite = MethodHandles.lookup().findVirtual(StringWriter.class, "write", methodType(void.class, char[].class, int.class, int.class)).bindTo(swr);
        MethodHandle swWrite4 = swWrite.asCollector(0, char[].class, 4);
        swWrite4.invoke('A', 'B', 'C', 'D', 1, 2);
//        assertEquals("BC", swr.toString());
        swWrite4.invoke('P', 'Q', 'R', 'S', 0, 4);
//        assertEquals("BCPQRS", swr.toString());
        swWrite4.invoke('W', 'X', 'Y', 'Z', 3, 1);
//        assertEquals("BCPQRSZ", swr.toString());

        //测试
        long current = System.currentTimeMillis();
        for (int i = 1; i <= 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long temp = System.currentTimeMillis();
                System.out.println(temp - current);
                current = temp;
            }
            mh1.invokeExact(new Object());
        }
        //测试性能
         test();
    }

    private static void test() throws Throwable{
        //   System.setProperty("sun.reflect.noInflation","true");
        long start = System.currentTimeMillis();

        MethodHandleTest methodHandle = new MethodHandleTest();

        MethodHandles.Lookup l = MethodHandles.lookup();
        MethodType t = MethodType.methodType(void.class, Object.class);
        MethodHandle mh = l.findVirtual(MethodHandleTest.class, "bar", t);

        MethodHandle methodHandle1 = mh.bindTo(methodHandle);
        for (int i = 0; i < 1000; i++) {
            methodHandle1.invokeExact(new Object());
        }

        Method method = MethodHandleTest.class.getDeclaredMethod("bar", Object.class);

        for (int i = 0; i < 100; i++) {
            method.invoke(methodHandle, new Object());
        }
        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

}
