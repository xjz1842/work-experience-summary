package concurrent.stream.limit;

public class TokenBucketDemo {

    private static long time = System.currentTimeMillis();

    private static int createTokenRate = 3;

    private static int size = 10;

    //当前令牌数
    private static int tokens = 0;

    private static boolean grant(){
        long now = System.currentTimeMillis();

        //在这段时间要产生的令牌数量
        int in = (int)((now - time)/1000 * createTokenRate);

        tokens = Math.min(size,tokens+in);

        time = now;

        System.out.println("tokens" + tokens);

        if(tokens > 0){
            --tokens;
            return true;
        }else{
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
