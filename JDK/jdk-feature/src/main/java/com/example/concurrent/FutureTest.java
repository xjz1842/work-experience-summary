package com.example.concurrent;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FutureTest {

    //(1)线程池单个线程，线程池队列元素个数为1
    private final static ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(1),new ThreadPoolExecutor.DiscardPolicy());

    public static void main(String[] args)throws Exception{

        //(2)添加任务one
        Future futureOne = executorService.submit(new Runnable() {
            @Override
            public void run() {

                System.out.println("start runable one");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

       // (3)添加任务one
        Future futureTwo = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("start runale two");
            }
        });

        //(4)添加任务three
        Future futureThree=null;
        try {
            futureThree = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("start runable three");
                }
            });
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        System.out.println("task one " + futureOne.get());//(5)等待任务one执行完毕
        System.out.println("task two " + futureTwo.get());//(6)等待任务two执行完毕
        System.out.println("task three " + (futureThree==null?null:futureThree.get()));// (7)等待任务three执行完毕

        executorService.shutdown();//(8)关闭线程池，阻塞直到所有任务执行完毕
    }
}
