package com.example.dynamic.proxy.jdk;

import com.example.dynamic.proxy.myjdk.Man;
import com.example.dynamic.proxy.myjdk.Zhangsan;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyTest {

    public static void main(String[] args) throws Throwable{
//        for(int i =0; i< 17; i++){
//            Class.forName("com.jdk14.dynamic.proxy.myjdk.Zhangsan").getConstructor().newInstance();
//        }
             Method method = Class.forName("com.example.dynamic.proxy.myjdk.Zhangsan").getMethod("findObject");

             Zhangsan zhangsan = new Zhangsan();

             //1.Constructor
             for(int i=0; i < 16; i++) {
                 method.invoke(zhangsan);
         }
//      System.setProperty("dk.proxy.ProxyGenerator.saveGeneratedFiles","true");

        //2.Method
        Man man = new Zhangsan();
        InvocationHandler handler = new JdkHandler(man);
        Man man1 = (Man)Proxy.newProxyInstance(JdkProxyTest.class.getClassLoader(),new Class[]{Man.class},handler);
        man1.findObject();

        //3.Method
        for(int i=0; i <=16; i++) {
            if(16 == i) {
                Class.forName("com.example.dynamic.proxy.myjdk.Zhangsan").getField("test");
            }
        }
    }



}
