package com.dynamic.proxy.myjdk;

import java.lang.reflect.Method;

public interface MyInvokerHandler {

    Object invoke(Object proxy,Method method,Object[] args)throws Throwable;

}
