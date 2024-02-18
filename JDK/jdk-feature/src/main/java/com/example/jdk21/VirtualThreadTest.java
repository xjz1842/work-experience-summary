package com.example.jdk21;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * 虚拟线程
 *
 * @author xjz18
 * @version V1.0
 * @date 2023/8/22 16:15
 */
public class VirtualThreadTest {

    public static void main(String[] args) {
        var start = System.currentTimeMillis();
        try (var executor = Executors.newWorkStealingPool()) {
            IntStream.range(0, 10_000).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofSeconds(1));
                return i;
            }));
        }  // executor.close() is called implicitly, and waits
        System.out.println("耗时" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> executor.submit(() -> {
                Thread.sleep(Duration.ofSeconds(1));
                return i;
            }));
        }  // executor.close() is called implicitly, and waits
        System.out.println("耗时" + (System.currentTimeMillis() - start));
    }
}
