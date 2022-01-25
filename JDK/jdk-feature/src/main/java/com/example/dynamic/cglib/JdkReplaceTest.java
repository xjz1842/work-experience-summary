package com.example.dynamic.cglib;



import com.example.dynamic.myjdk.Man;
import com.example.dynamic.myjdk.Zhangsan;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkReplaceTest {

    public static void main(String[] args) throws Throwable{

        System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles","true");

        Man zhangsan = new Zhangsan();

        Man proxy = (Man)Proxy.newProxyInstance(zhangsan.getClass().getClassLoader(),
                Zhangsan.class.getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("before");
                        method.invoke(zhangsan);
                        System.out.println("after");
                        return null;
                    }
                });
        proxy.findObject();
    }

}
