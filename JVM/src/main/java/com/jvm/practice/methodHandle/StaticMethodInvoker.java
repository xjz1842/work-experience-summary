package com.jvm.practice.methodHandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class StaticMethodInvoker {

    public void bar(Object o) {
    }

    public static void target(int i) {

    }

    public static void main(String[] args) throws Throwable {
//        MethodHandles.Lookup l = MethodHandles.lookup();
//        MethodType t = MethodType.methodType(void.class, Object.class);
//        MethodHandle mh = l.findVirtual(Foo.class, "bar", t);
//        long current = System.currentTimeMillis();
//        for (int i = 1; i <= 2_000_000_000; i++) {
//            if (i % 100_000_000 == 0) {
//                long temp = System.currentTimeMillis();
//                System.out.println(temp - current);
//                current = temp;
//            }
//            mh.invokeExact(new Foo(), new Object());
//        }
//        long current = System.currentTimeMillis();
//        int x = 2;
//        for (int i = 1; i <= 2_000_000_000; i++) {
//            if (i % 100_000_000 == 0) {
//                long temp = System.currentTimeMillis();
//                System.out.println(temp - current);
//                current = temp;
//            }
//            ((IntConsumer) j -> Foo.target(j)).accept(128);
//            ((IntConsumer) j -> Foo.target(x + j)).accept(128);
        // ((IntConsumer) Test::target.accept(128);

        MethodHandles.Lookup l = MethodHandles.lookup();
        MethodType t = MethodType.methodType(void.class, int.class);
        MethodHandle mh = l.findStatic(StaticMethodInvoker.class, "target", t);

        long current = System.currentTimeMillis();
        for (int i = 1; i <= 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long temp = System.currentTimeMillis();
                System.out.println(temp - current);
                current = temp;
            }
            mh.invokeExact(128);
        }
    }


}
