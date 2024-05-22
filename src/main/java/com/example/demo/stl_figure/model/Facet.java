package com.example.demo.stl_figure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Facet implements Serializable {
    private final List<Edge> edges;
    private final Normal normal;

    public Facet(List<Edge> edges, Normal normal) {
        this.edges = edges;
        this.normal = normal;
    }

    public float calculateArea() {
        List<Vertex> vertices = getVertices();

        double[] crossProduct = getCrossProduct(vertices);

        // Calcul de la norme du produit vectoriel
        float crossProductNorm = (float) Math.sqrt(
                crossProduct[0] * crossProduct[0] +
                        crossProduct[1] * crossProduct[1] +
                        crossProduct[2] * crossProduct[2]
        );

        return 0.5f * crossProductNorm;
    }

    private static double[] getCrossProduct(List<Vertex> vertices) {
        float[] A = vertices.get(0).getCoord();
        float[] B = vertices.get(1).getCoord();
        float[] C = vertices.get(2).getCoord();

        // Calcul des vecteurs AB et AC
        double[] AB = {B[0] - A[0], B[1] - A[1], B[2] - A[2]};
        double[] AC = {C[0] - A[0], C[1] - A[1], C[2] - A[2]};

        // Calcul du produit vectoriel AB x AC
        return new double[]{
                AB[1] * AC[2] - AB[2] * AC[1],
                AB[2] * AC[0] - AB[0] * AC[2],
                AB[0] * AC[1] - AB[1] * AC[0]
        };
    }

    @Override
    public String toString() {
        return "Facet{" +
                ", polygons=" + edges +
                ", normal=" + normal +
                '}';
    }

    public List<Vertex> getVertices() {
        List<Vertex> vertices = new ArrayList<>();

        for(Edge edge : edges) {
            vertices.add(edge.getEdgeStart());
        }
        return vertices;
    }
}
