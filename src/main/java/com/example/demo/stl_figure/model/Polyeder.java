package com.example.demo.stl_figure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Polyeder implements Serializable {
    private final String name;
    private final List<Facet> facets;

    public Polyeder(String name, List<Facet> facets) {
        this.name = name;
        this.facets = facets;
    }

    public float calculateArea() {
        float sum = 0;

        for(Facet facet : facets) {
            sum += facet.calculateArea();
        }
        return sum;
    }

    public List<Facet> getFacets() {
        return this.facets;
    }

    public List<Vertex> getAllTops() {
        List<Vertex> list = new ArrayList<>();

        for(Facet facet : facets) {
            list.addAll(facet.getVertices());
        }
        return list;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Polyedre{" +
                "name='" + name + '\'' +
                ", facets=" + facets +
                '}';
    }

    public float calculateVolume() {
        float volume = 0;
        List<Vertex> vertices = this.getAllTops();

        Vertex reference = vertices.get(0);

        for (int i = 1; i < vertices.size() - 1; i++) {
            double[] A = {reference.getX(), reference.getY(), reference.getZ()};
            double[] B = {vertices.get(i).getX(), vertices.get(i).getY(), vertices.get(i).getZ()};
            double[] C = {vertices.get(i + 1).getX(), vertices.get(i + 1).getY(), vertices.get(i + 1).getZ()};

            volume += calculateTriangleVolume(A, B, C);
        }

        return volume;
    }

    private float calculateTriangleVolume(double[] A, double[] B, double[] C) {
        float volume = (float) Math.abs(
                A[0] * (B[1] * C[2] - B[2] * C[1]) +
                        A[1] * (B[2] * C[0] - B[0] * C[2]) +
                        A[2] * (B[0] * C[1] - B[1] * C[0])
        );

        volume /= 6.0F;

        return volume;
    }
}