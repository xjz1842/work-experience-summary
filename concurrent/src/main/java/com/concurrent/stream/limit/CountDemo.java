package com.concurrent.stream.limit;


/**
 * http://blog.51cto.com/zhangfengzhe/2066683
 */
public class CountDemo {
    private static long timeStamp = System.currentTimeMillis();

    //限制1s内100个请求
    private static long limitCount = 100;
    private static long internal = 1000;

    //请求数
    private static long reqCount = 0;

    public static boolean grant(){
        long now = System.currentTimeMillis();

        if(now < timeStamp + internal){
            if(reqCount < limitCount){
                ++reqCount;
                return true;
            }else{
                return false;
            }
        }else {
            timeStamp = System.currentTimeMillis();
            reqCount = 0;
            return false;
        }
    }


    public static void main(String[] args) {

        for(int i=0; i < 500; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(grant()){
                        System.out.println("执行业务逻辑");
                    }else{
                        System.out.println("限流");
                    }
                }
            }).start();
        }
    }
}
