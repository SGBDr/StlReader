package com.example.demo.stl_figure.model;

import java.io.Serializable;

public class Polygon implements Serializable {
    private Top firstTop;
    private Top secondTop;

    public Polygon(Top firstTop, Top secondTop) {
        this.firstTop = firstTop;
        this.secondTop = secondTop;
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "firstTop=" + firstTop +
                ", secondTop=" + secondTop +
                '}';
    }
}