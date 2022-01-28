package com.concurrent.lock.semaphore;

import java.util.concurrent.Semaphore;

public class ReceivingThread extends Thread {

    Semaphore semaphore = null;

    public ReceivingThread(Semaphore semaphore){
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        while(true){
            this.semaphore.release();
            System.out.println("receive");
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            //receive signal, then do something...
        }
    }
}
