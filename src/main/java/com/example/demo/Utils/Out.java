package com.example.demo.Utils;

public class Out {

    public static void print(String message) {
        System.out.println(message);
    }

    public static void exception(String message) {
        throw new RuntimeException(message);
    }
}
