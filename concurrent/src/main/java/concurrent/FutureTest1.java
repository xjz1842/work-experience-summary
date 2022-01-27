package concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureTest1 {

    public static void main(String[] args) throws Exception{
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        long start = System.currentTimeMillis();
        Future<Integer> resultFuture1 = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return method1() + method2();
            }
        });

        Future<Integer> resultFuture2 = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Future<Integer> resultFuture3 = executorService.submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return method3();
                    }
                });
                Future<Integer> resultFuture4 = executorService.submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return method4();
                    }
                });
                return method5() + resultFuture3.get() + resultFuture4.get();
            }
        });
        int result = resultFuture1.get() + resultFuture2.get();

        System.out.println("result = " + result + ", total cost " + (System.currentTimeMillis() - start) + " ms");
        executorService.shutdown();
    }

    static int method1() {
        delay200ms();
        return 1;
    }

    static int method2() {
        delay200ms();
        return 2;
    }

    static int method3() {
        delay200ms();
        return 3;
    }

    static int method4() {
        delay200ms();
        return 4;
    }

    static int method5() {
        delay200ms();
        return 5;
    }
    static void delay200ms() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
