package com.example.proxy.myjdk;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyProxy {

    private static final String rt = "\r";

    public static Object newProxyInstance(MyClassLoader loader, Class<?> interfaces, MyInvokerHandler h)
            throws IllegalArgumentException {
        if (h == null)
            return null;

        //根据接口生成代理类
        Method[] methods = interfaces.getMethods();

        StringBuffer proxyClassString = new StringBuffer();
        proxyClassString.append("package ")
                .append(loader.getProxyClassPackage()).append(";").append(rt)
                .append("import java.lang.reflect.Method;").append(rt)
                .append("public class $MyProxy0 implements ").append(interfaces.getName()).append("{").append(rt)
                .append("MyInvokerHandler h;").append(rt)
                .append("public $MyProxy0(MyInvokerHandler h){ ").append("this.h = h;}").append(rt)
                .append(getMethodString(methods, interfaces)).append("}");

        //写入java文件 进行编译
        String fileName = loader.getDir() + File.separator + "$MyProxy0.java";

        File myProxyFile = new File(fileName);

        try {
            compile(proxyClassString, myProxyFile);

            //利用自定义的loader加载
            Class $myProxy0 = loader.findClass("$MyProxy0");

            Constructor constructor = $myProxy0.getConstructor(MyInvokerHandler.class);

            Object result = constructor.newInstance(h);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMethodString(Method[] methods, Class interfaces) {
        StringBuffer sb = new StringBuffer();

        for (Method method : methods) {
            sb.append("@Override").append(rt);
            sb.append("public void ").append(method.getName())
                    .append("()").append("throws Throwable{ ")
                    .append("Method method1 = ").append(interfaces.getName())
                    .append(".class.getMethod(\"").append(method.getName())
                    .append("\", new Class[]{});")
                    .append("this.h.invoke(this,method1,null);}")
                    .append(rt);
        }

        return sb.toString();
    }


    private static void compile(StringBuffer stringBuffer, File proxyFile) throws IOException {
        FileWriter fileWriter = new FileWriter(proxyFile);
        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);

        Iterable javaFileObjects = standardJavaFileManager.getJavaFileObjects(proxyFile);

        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, standardJavaFileManager, null, null, null, javaFileObjects);

        task.call();

        standardJavaFileManager.close();
    }

}
