package com.example.demo.stl_figure.model;

import java.io.Serializable;
import java.util.List;

public class Facet implements Serializable {
    private Top normal;
    private List<Top> tops;
    private List<Polygon> polygons;

    public Facet(Top normal, List<Top> tops, List<Polygon> polygons) {
        this.normal = normal;
        this.tops = tops;
        this.polygons = polygons;
    }

    public double calculateSurface() {
        Top top1 = this.tops.get(0);
        Top top2 = this.tops.get(1);
        Top top3 = this.tops.get(2);

        // Calculate the vectors representing two sides of the triangle
        Top side1 = new Top(top2.getX() - top1.getX(), top2.getY() - top1.getY(), top2.getZ() - top1.getZ());
        Top side2 = new Top(top3.getX() - top1.getX(), top3.getY() - top1.getY(), top3.getZ() - top1.getZ());

        // Calculate the cross product of the two sides to get the normal vector
        Top crossProduct = crossProduct(side1, side2);

        // Calculate the magnitude of the cross product vector
        double magnitude = Math.sqrt(
                crossProduct.getX() * crossProduct.getX() +
                        crossProduct.getY() * crossProduct.getY() +
                        crossProduct.getZ() * crossProduct.getZ()
        );

        // Area of the triangle is half of the magnitude of the cross product
        double area = magnitude / 2.0;

        return area;
    }

    private Top crossProduct(Top v1, Top v2) {
        // Calculate the cross product of two vectors
        double x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
        double y = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
        double z = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        return new Top(x, y, z);
    }

    @Override
    public String toString() {
        return "Facet{" +
                "normal=" + normal +
                ", tops=" + tops +
                ", polygons=" + polygons +
                '}';
    }

    public List<Top> getTops() {
        return this.tops;
    }

    public Top calculateCenter() {
        Top top1 = this.tops.get(0);
        Top top2 = this.tops.get(1);
        Top top3 = this.tops.get(2);

        double xx = (top1.getX() + top2.getX() + top3.getX()) / 3;
        double yy = (top1.getY() + top2.getY() + top3.getY()) / 3;
        double zz = (top1.getZ() + top2.getZ() + top3.getZ()) / 3;

        return new Top(xx, yy, zz);
    }
}
