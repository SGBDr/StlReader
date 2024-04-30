package com.example.demo.stl_figure.model;

import java.io.Serializable;
import java.util.List;

public class Polyhedron implements Serializable {
    private String name;
    private List<Facet> facets;
    private List<Top> allTops;

    public Polyhedron(String name, List<Top> allTops, List<Facet> facets) {
        this.name = name;
        this.facets = facets;
        this.allTops = allTops;
    }

    public Top calculateCenter() {
        double x = 0.0, y = 0.0, z = 0.0;

        for(Facet facet : this.facets) {
            Top center = facet.calculateCenter();
            x += center.getX() / this.facets.size();
            y += center.getY() / this.facets.size();
            z += center.getZ() / this.facets.size();
        }

        return new Top(x, y, z);
    }

    public List<Top> getAllTops() {
        return this.allTops;
    }

    public double calculateVolume() {
        double volume = 0.0;

        Top center = calculateCenter();

        for (Facet face : this.facets) {
            for (int i = 0; i < face.getTops().size() - 2; i++) {
                Top v1 = face.getTops().get(0);
                Top v2 = face.getTops().get(i + 1);
                Top v3 = face.getTops().get(i + 2);

                double tetrahedronVolume = signedVolumeOfTetrahedron(center, v1, v2, v3);
                volume += tetrahedronVolume;
            }
        }

        return Math.abs(volume);
    }

    private static double signedVolumeOfTetrahedron(Top v0, Top v1, Top v2, Top v3) {
        double v321 = v3.getX() * v2.getY() * v1.getZ();
        double v231 = v2.getX() * v3.getY() * v1.getZ();
        double v312 = v3.getX() * v1.getY() * v2.getZ();
        double v132 = v1.getX() * v3.getY() * v2.getZ();
        double v213 = v2.getX() * v1.getY() * v3.getZ();
        double v123 = v1.getX() * v2.getY() * v3.getZ();

        return (1.0 / 6.0) * (-v321 + v231 + v312 - v132 - v213 + v123);
    }

    public double calculateSurface() {
        double surface = 0.0;

        for(Facet facet : this.facets)
            surface += facet.calculateSurface();

        return surface;
    }

    @Override
    public String toString() {
        return "Polyhedron{" +
                "name='" + name + '\'' +
                ", facets=" + facets +
                '}';
    }

    public List<Facet> getFacets() {
        return this.facets;
    }
}