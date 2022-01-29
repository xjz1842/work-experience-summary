package com.concurrent.cas;


import java.util.concurrent.atomic.AtomicBoolean;

public class CASDemo {

    private AtomicBoolean locked = new AtomicBoolean(false);

    public  void unlock() {
        this.locked.set(false);
        System.out.println("解锁成功");
    }

    public void lock() {
        while(this.locked.compareAndSet(false, true)) {
            // busy wait - until compareAndSet() succeeds
            System.out.println("加锁成功");
        }
    }

    public static void main(String[] args) {
        CASDemo casDemo = new CASDemo();
        casDemo.lock();
        casDemo.unlock();
    }
}
