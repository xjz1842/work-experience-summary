package com.example.dynamic.proxy.myjdk;

public class Client {

    public static void main(String[] args) throws Throwable{
        Man man = new Zhangsan();

        MyHandler myHandler = new MyHandler(man);

        Man proxyMan = (Man)MyProxy.newProxyInstance(new MyClassLoader("/Users/zxj/github/jdk14-demo/src/main/java/com/jdk14/demo/dynamic/myjdk","com.jdk14.dynamic.proxy.myjdk"),Man.class,myHandler);

        System.out.println(proxyMan.getClass().getName());

        proxyMan.findObject();
    }

}
