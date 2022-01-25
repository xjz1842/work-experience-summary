package com.example.stream;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamAPITest {

    public static void main(String[] args) {

      //遍历 1~100
     IntStream.iterate(1, i -> i < 100, i -> i + 1).forEach(System.out::println);

     //Optional对象转换成Stream
     Stream<Integer> s = Optional.of(1).stream();

     IntStream intStream = IntStream.range(1,100);
     //删除100中的1-4
     intStream.dropWhile(k -> k < 5).forEach(ele -> System.out.println(ele));

     IntStream intStream1 = IntStream.range(1,100);
     //取出100中的1-4
     intStream1.takeWhile(k -> k < 5).forEach(ele -> System.out.println(ele));

      //允许为空的Stream
      Stream.ofNullable("2").forEach(e -> System.out.println(e));

    }

}

