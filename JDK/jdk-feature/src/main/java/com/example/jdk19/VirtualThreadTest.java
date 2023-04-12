package com.example.jdk19;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * jdk虚拟线程
 *
 * @author xjz18
 * @version V1.0
 * @date 2023/4/12 17:37
 */
public class VirtualThreadTest {

    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    System.out.println(i);
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }
}
