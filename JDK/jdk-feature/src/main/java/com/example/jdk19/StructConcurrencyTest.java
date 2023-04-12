package com.example.jdk19;

import jdk.incubator.concurrent.StructuredTaskScope;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * TODO
 *
 * @author xjz18
 * @version V1.0
 * @date 2023/4/12 17:53
 */
public class StructConcurrencyTest {


    /**
     *  Response handle() throws ExecutionException, InterruptedException {
     *         try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
     *             Future<String>  user  = scope.fork(() -> findUser());
     *             Future<Integer> order = scope.fork(() -> fetchOrder());
     *
     *             scope.join();           // Join both forks
     *             scope.throwIfFailed();  // ... and propagate errors
     *             // Here, both forks have succeeded, so compose their results
     *             return new Response(user.resultNow(), order.resultNow());
     *         }
     *     }
     */
}
