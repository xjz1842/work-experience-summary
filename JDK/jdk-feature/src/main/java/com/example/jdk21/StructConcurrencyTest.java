package com.example.jdk21;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

/**
 * 结构化并发
 *
 * @author xjz18
 * @version V1.0
 * @date 2024/2/18 15:25
 */
public class StructConcurrencyTest {
    public static void main(String[] args) throws Exception {
        var start = System.currentTimeMillis();
        System.out.println(handle());
        System.out.println(System.currentTimeMillis() - start);
    }

    static Response handle() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Supplier<String> user = scope.fork(StructConcurrencyTest::findUser);
            Supplier<Integer> order = scope.fork(StructConcurrencyTest::fetchOrder);
            scope.join()            // Join both subtasks
                    .throwIfFailed();  // ... and propagate errors
            // Here, both subtasks have succeeded, so compose their results
            return new Response(user.get(), order.get());
        }
    }

    private static Integer fetchOrder() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("fetch order");
        return 1;
    }

    private static String findUser() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("fetch user");
        return "str";
    }

    private record Response(String fetchOrder,
                            Integer fetchUser) {
    }
}
