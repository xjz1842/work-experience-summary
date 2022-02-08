package com.javase.io.nio;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathDemo {

    public static void main(String[] args) {
        //绝对路径
        Path absPath = Paths.get("myfile.txt");
        //相对路径
        Path projects = Paths.get("d:\\data", "projects");

        Path basePath = Paths.get("/data");
        Path path     = Paths.get("/data/subdata/subsubdata/myfile.txt");

        Path basePathToPath = basePath.relativize(path);
        Path pathToBasePath = path.relativize(basePath);

        System.out.println(basePathToPath);
        System.out.println(pathToBasePath);

        String originalPath =
                "d:\\data\\projects\\a-project\\..\\another-project";

        Path path1 = Paths.get(originalPath);
        System.out.println("path1 = " + path1);

        Path path2 = path1.normalize();
        System.out.println("path2 = " + path2);
    }
}
