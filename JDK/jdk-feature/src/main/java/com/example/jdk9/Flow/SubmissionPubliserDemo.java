package com.example.jdk9.Flow;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class SubmissionPubliserDemo {

    public static void main(String[] args)throws Exception {

        //Create Publisher
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        //Create Processor and Subscriber
        MyFilterProcessor<String, String> filterProcessor = new MyFilterProcessor<>(s -> s.equals("x"));
        MySubscriber<String> subscriber = new MySubscriber<>();
        //Chain Processor and Subscriber
        publisher.subscribe(filterProcessor);
        filterProcessor.subscribe(subscriber);
        System.out.println("Publishing Items...");
        String[] items = {"1", "x", "2", "x", "3", "x"};
        Arrays.asList(items).stream().forEach(i -> publisher.submit(i));
        publisher.close();
        Thread.sleep(2000);
    }

    public static class MySubscriber<T> implements Flow.Subscriber<T> {
        private Flow.Subscription subscription;
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1); //这里要使用Long.MAX_VALUE就会被认为获取无穷的数据。
        }
        @Override
        public void onNext(T item) {
            System.out.println("Got : " + item);
            subscription.request(1); //这里也可以使用Long.MAX_VALUE
        }
        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }
        @Override
        public void onComplete() {
            System.out.println("Done");
        }
    }

    public static class MyFilterProcessor<T,K extends T> extends SubmissionPublisher<K> implements Flow
            .Processor<T, K> {
        private Function<? super T, Boolean> function;
        private Flow.Subscription subscription;
        public MyFilterProcessor(Function<? super T, Boolean> function) {
            super();
            this.function = function;
        }
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }
        @Override
        public void onNext(T item) {
            if (!(boolean) function.apply(item)) {
                submit((K)item);
            }
            subscription.request(1);
        }
        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }
        @Override
        public void onComplete() {
            close();
        }
    }
}
