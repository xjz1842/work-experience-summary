package com.example.dynamic.myjdk;

import java.io.*;

/**
 * @author xjz
 */
public class MyClassLoader extends ClassLoader {

    private File dir;

    private String proxyClassPackage;

    public String getProxyClassPackage() {
        return proxyClassPackage;
    }

    public File getDir() {
        return dir;
    }

    public MyClassLoader(String path, String proxyClassPackage) {
        this.dir = new File(path);
        this.proxyClassPackage = proxyClassPackage;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        if (dir != null) {
            File classFile = new File(dir, name + ".class");

            if (classFile.exists()) {
                try {
                    byte[] classBytes = copyToByteArray(classFile);

                    return defineClass(proxyClassPackage + "." + name, classBytes, 0, classBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.findClass(name);
    }

    private byte[] copyToByteArray(File file) throws IOException {

        InputStream inputStream = new FileInputStream(file);

        byte[] result;
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, bytesRead);
            }
            result = outputStream.toByteArray();
        }finally {
            inputStream.close();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {

        InputStream inputStream = new FileInputStream("/Users/zxj/github/jdk14-demo/src/main/java/com/jdk14/demo/dynamic/myjdk/$MyProxy0.class");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, bytesRead);
            }
        } finally {
            inputStream.close();
        }

    }
}
