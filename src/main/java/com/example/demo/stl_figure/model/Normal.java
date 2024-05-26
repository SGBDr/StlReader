package com.example.demo.stl_figure.model;

import java.io.Serializable;

public class Normal implements Serializable {
    private final double nX;
    private final double nY;
    private final double nZ;

    public Normal(Vertex normal) {
        float[] coord = normal.getCoord();
        double norm = Math.sqrt(Math.pow(coord[0], 2) + Math.pow(coord[1], 2) + Math.pow(coord[2], 2));
        this.nX = coord[0]/norm;
        this.nY = coord[1]/norm;
        this.nZ = coord[2]/norm;
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