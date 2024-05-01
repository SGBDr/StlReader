package com.example.demo.stl_figure;

public class Constant {
    public static String line1 = "Invalid solid format. line 1";
    public static String line = "Invalid facet format. line ";
    public static String binaryReadImplementation = "Binary STL file parsing is not implemented yet.";
    public static String notDouble = "Some value in you file are not double value";
    public static String vertex = "vertex ";
    public static String solid = "solid ";

    public static String normal = "facet normal ";
    public static String space = " ";
    public static String empty = "";

    public static String lineXtoX(int i) {
        return "Invalid vertex format. Look between line " + (i + 1) + " and " + (i + 3);
    }
}
