package com.example.demo.Utils;

public class Exception {
    public static void raise(String message) {
        throw new RuntimeException(message);
    }
}
