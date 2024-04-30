package com.example.demo.stl_figure.manager.stl;

import com.example.demo.stl_figure.model.Polyhedron;

import java.net.URISyntaxException;

public interface STLReader {
    Polyhedron readSTLFile(String fileName) throws URISyntaxException;
}
