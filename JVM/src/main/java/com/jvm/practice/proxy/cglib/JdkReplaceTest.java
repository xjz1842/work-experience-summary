package com.jvm.practice.proxy.cglib;



import com.jvm.practice.proxy.myjdk.Man;
import com.jvm.practice.proxy.myjdk.Zhangsan;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkReplaceTest {

    public static void main(String[] args) throws Throwable{

        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles","true");

        Man zhangsan = new Zhangsan();

        Man proxy = (Man)Proxy.newProxyInstance(zhangsan.getClass().getClassLoader(),
                Zhangsan.class.getInterfaces(), (proxy1, method, args1) -> {
                    System.out.println("before");
                    method.invoke(zhangsan);
                    System.out.println("after");
                    return null;
                });
        proxy.findObject();
    }

}
