package com.example.demo.stl_figure.manager.stl;

import com.example.demo.stl_figure.model.Facet;

public class Synchroniser {
    public static void calculateSurface(Facet facet) {
        new Thread(() -> {
            double surface = facet.calculateArea();
            System.out.println("The surface of " + facet + "\nis : " + surface);
        }).start();
    }
}
