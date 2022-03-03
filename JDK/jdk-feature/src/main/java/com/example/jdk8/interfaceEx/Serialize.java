package com.example.jdk8.interfaceEx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Serialize {

    interface I extends Serializable {}

    interface F<T, R> extends Serializable {
        R apply(T t);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        F<I, Integer> f = I::hashCode;
        try (OutputStream os = Files.newOutputStream(Paths.get("o"));
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
             oos.writeObject(f);
        }
        try (InputStream is = Files.newInputStream(Paths.get("o"));
             ObjectInputStream ois = new ObjectInputStream(is)) {
            f = (F<I, Integer>) ois.readObject();
        }
        System.err.println(f.apply(new I() {}));
    }
}
