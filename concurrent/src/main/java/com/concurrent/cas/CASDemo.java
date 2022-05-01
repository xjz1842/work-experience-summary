package com.concurrent.cas;


import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

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
        System.out.println(Charset.defaultCharset());
        CASDemo casDemo = new CASDemo();
        casDemo.lock();
        casDemo.unlock();
        LockSupport.park();
    }
}
