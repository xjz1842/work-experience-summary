package com.concurrent.lockfree;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalLeakTest {

    final static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,5,1, TimeUnit.MINUTES,new LinkedBlockingDeque<>());

    final static ThreadLocal<Long> localVariable = new ThreadLocal<>();

    public static void main(String[] args)throws Exception {

        // (3)
        for (int i = 0; i < 500; ++i) {
            poolExecutor.execute(() -> {
                // (4)
                localVariable.set(1L);
                // (5)
                System.out.println("use local varaible");
                localVariable.remove();
            });

            Thread.sleep(1000);
        }
        // (6)
        System.out.println("pool execute over");

    }

}


