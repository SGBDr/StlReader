package com.example.demo.stl_figure.model;

import java.io.Serializable;

public class Normal implements Serializable {
    private final double nX;
    private final double nY;
    private final double nZ;

    public Normal(Vertex normal) {
        float[] coord = normal.getCoord();
        this.nX = coord[0];
        this.nY = coord[1];
        this.nZ = coord[2];
    }

    @Override
    public String toString() {
        return "Normal{" +
                "nX=" + nX +
                ", nY=" + nY +
                ", nZ=" + nZ +
                '}';
    }
}