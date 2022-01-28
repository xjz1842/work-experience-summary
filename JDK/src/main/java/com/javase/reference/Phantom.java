package com.javase.reference;


import java.lang.ref.Cleaner;

public class Phantom {

    public static void main(String[] args) {
          Cleaner cleaner = Cleaner.create();
          Object ref = new Object();

          cleaner.register(ref,()->{
              System.out.println("清理资源");
          });
          // 手动触发gc
         System.gc();
    }
}
