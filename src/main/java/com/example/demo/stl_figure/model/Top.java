package com.example.demo.stl_figure.model;

import java.io.Serializable;

public class Top implements Serializable {
    private double x;
    private double y;
    private double z;

    public Top(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return (float) this.x;
    }

    public float getY() {
        return (float) y;
    }

    public float getZ() {
        return (float) z;
    }

    @Override
    public String toString() {
        return "Top{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public void mul(double defaultSize) {
    }
}