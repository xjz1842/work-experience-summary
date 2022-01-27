package concurrent;

public class TestSynchronized {


    private  static  final  TestSynchronized instance = new TestSynchronized();

    private TestSynchronized(){

    }

    public static TestSynchronized getInstance(){
        return instance;
    }

    public void test1() {
        synchronized (this) {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public void test2() {
        synchronized (TestSynchronized.class) {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public synchronized void isSyncA() {
        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }

    public synchronized void isSyncB() {
        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }

    public static synchronized void cSyncA() {
        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }

    }

    public static synchronized void cSyncB() {
        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }

    public static void main(String[] args) {

        TestSynchronized testSynchronized =  TestSynchronized.getInstance();

        //同一个实例，不同的synchronized方法，对象锁有约束（同一个对象——对象锁）——a. x.isSyncA()与x.isSyncB()
        Thread test1 = new Thread(new Runnable() {
            public void run() {
                testSynchronized.test1();
            }
        }, "test1");

        test1.start();

        Thread test2 = new Thread(new Runnable() {
            public void run() {
                testSynchronized.test1();
            }
        }, "test2");
        test2.start();


    }
}