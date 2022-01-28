package com.concurrent.lock;


import java.util.function.Consumer;

/**
 * reference:
 * http://tutorials.jenkov.com/java-concurrency/synchronized.html
 *
 * The synchronized keyword can be used to mark four different types of blocks:
 *
 *  1. Instance methods
 *  2. Static methods
 *  3. Code blocks inside instance methods
 *  4. Code blocks inside static methods
 */
public class SynchronizedExample {

    public static class Counter{

        long count = 0;

        public synchronized void add(long value){
            this.count += value;
        }
    }

    public static class CounterThread extends Thread{

        protected Counter counter = null;

        public CounterThread(Counter counter){
            this.counter = counter;
        }
        @Override
        public void run() {
            for(int i= 0; i < 10; i++){
                counter.add(i);
            }
        }
    }


    public static void main(String[] args) throws Exception{
        // case 1
        Consumer<String> func = (String param) -> {

            synchronized(SynchronizedExample.class) {

                System.out.println(
                        Thread.currentThread().getName() +
                                " step 1: " + param);

                try {
                    Thread.sleep( (long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(
                        Thread.currentThread().getName() +
                                " step 2: " + param);
            }
        };

        Thread thread1 = new Thread(() -> {
            func.accept("Parameter");
        }, "Thread 1");

        Thread thread2 = new Thread(() -> {
            func.accept("Parameter");
        }, "Thread 2");

        thread1.start();
        thread2.start();

        // case 2
        Counter counter = new Counter();
        Thread  threadA = new CounterThread(counter);
        Thread  threadB = new CounterThread(counter);

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        System.out.println(counter.count);
    }
}
