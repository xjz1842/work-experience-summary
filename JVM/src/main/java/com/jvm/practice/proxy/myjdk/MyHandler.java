package com.jvm.practice.proxy.myjdk;

import java.lang.reflect.Method;

public class MyHandler implements MyInvokerHandler {

    private Man man;

    public MyHandler(Man man) {
        this.man = man;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result =  method.invoke(man,null);
        after();
        return result;
    }

    private void before() {
        System.out.println("---- befote ----");
    }

    private void after() {
        System.out.println("=----after-------");
    }
}
