package com.clean;

import java.lang.ref.Cleaner;

public class CleaningExample implements AutoCloseable {

    private static final Cleaner cleaner = Cleaner.create();

    static class State implements Runnable {
        State() {}

        @Override
        public void run() {
            System.out.println("清理资源");
        }
    }

    private final State state;
    private final Cleaner.Cleanable cleanable;

    public CleaningExample() {
        this.state = new State();
        this.cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        System.out.println("回调用");
        cleanable.clean();
    }
}
