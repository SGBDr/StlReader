package com.example.demo.stl_figure.model;

import java.io.Serializable;

public class Vertex implements Serializable {
    private final float x;
    private final float y;
    private final float z;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {return x;}
    public float getY() {return y;}
    public float getZ() {return z;}

    @Override
    public String toString() {
        return "Top{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public float[] getCoord() {
        return new float[]{(int) x, (int) y, (int) z};
    }

}
