package com.example.proxy.cglib;


import com.example.proxy.myjdk.Zhangsan;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibDemo {

    public static void main(String[] args)throws Throwable {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Zhangsan.class);

        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                Object res = methodProxy.invokeSuper(o, args);
                System.out.println("after");
                return res;
            }
        });

        Zhangsan zhangsan = (Zhangsan) enhancer.create();

        zhangsan.toString();
    }

}
