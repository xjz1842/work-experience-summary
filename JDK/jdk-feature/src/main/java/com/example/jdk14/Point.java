package com.example.jdk14;

public record Point(int lo, int hi) {

    public Point {
        if (lo > hi)  /* referring here to the implicit constructor parameters */
            throw new IllegalArgumentException(String.format("(%d,%d)", lo, hi));
    }

    public static void main(String[] args) {
        Point point = new Point(1,2);
        System.out.println(point.lo());
    }
}
