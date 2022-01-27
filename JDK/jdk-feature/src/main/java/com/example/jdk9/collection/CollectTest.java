package com.example.jdk9.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectTest {


    public static void main(String[] args) {
        //list
       var list = List.of("a","b","c");
       list.stream().forEach(k-> System.out.println(k));

       //set
        var set  = Set.of("a","b","c");
        set.forEach(k-> System.out.println(k));

        //map
        var  map = Map.of(1,"a",2,"b",3,"c");
        map.forEach((k,v)-> System.out.println(k+"---" +v));


    }
}
