package com.example.demo.stl_figure.model;

import java.io.Serializable;

public class Edge implements Serializable {
    private final Vertex edgeStart;
    private final Vertex edgeEnd;

    public Edge(Vertex edgeStart, Vertex edgeEnd) {
        this.edgeStart = edgeStart;
        this.edgeEnd = edgeEnd;
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "firstTop=" + edgeStart +
                ", secondTop=" + edgeEnd +
                '}';
    }

    public Vertex getEdgeStart() {
        return edgeStart;
    }
}