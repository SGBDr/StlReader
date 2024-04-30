package com.example.demo.stl_figure.manager.stl;

import com.example.demo.stl_figure.model.Facet;

public class Sync {
    public static void calculateSurface(Facet facet) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                double surface = facet.calculateSurface();
                System.out.println("The surface of " + facet + "\nis : " + surface);
            }
        });
        thread.run();
    }
}
