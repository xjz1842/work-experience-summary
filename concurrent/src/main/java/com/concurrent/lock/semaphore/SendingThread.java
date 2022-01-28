package com.concurrent.lock.semaphore;

import java.util.concurrent.Semaphore;

public class SendingThread extends Thread {

    Semaphore semaphore = null;

    public SendingThread(Semaphore semaphore){
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        while(true){
            //do something, then signal
            try {
                this.semaphore.acquire();
                System.out.println("send");
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
