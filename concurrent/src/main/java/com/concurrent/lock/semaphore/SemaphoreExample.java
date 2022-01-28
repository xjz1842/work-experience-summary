package com.concurrent.lock.semaphore;

import java.util.concurrent.Semaphore;

public class SemaphoreExample {

    private boolean signal = false;

    public synchronized void take() {
        this.signal = true;
        this.notify();
    }

    public synchronized void release() throws InterruptedException {
        while (!this.signal) wait();
        this.signal = false;
    }

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);

        SendingThread sender = new SendingThread(semaphore);

        ReceivingThread receiver = new ReceivingThread(semaphore);

        receiver.start();
        sender.start();
    }
}
