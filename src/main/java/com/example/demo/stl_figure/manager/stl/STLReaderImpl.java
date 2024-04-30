package com.example.demo.stl_figure.manager.stl;

import com.example.demo.HelloApplication;
import com.example.demo.stl_figure.exception.Exception;
import com.example.demo.stl_figure.model.Facet;
import com.example.demo.stl_figure.model.Polygon;
import com.example.demo.stl_figure.model.Polyhedron;
import com.example.demo.stl_figure.model.Top;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class STLReaderImpl implements STLReader {
    @Override
    public Polyhedron readSTLFile(String fileName) throws URISyntaxException {
        File file = new File(HelloApplication.class.getResource("data/" + fileName).toURI());

        try (InputStream inputStream = new FileInputStream(file)) {
            if (false) {
                // Read binary STL file
                // Implement binary STL file parsing
                throw new UnsupportedOperationException("Binary STL file parsing is not implemented yet.");
            } else {
                // Read ASCII STL file
                return readAsciiSTL(inputStream);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readAllLines(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        List<String> fileLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null)
            fileLines.add(line.trim());

        return fileLines;
    }

    private Polyhedron readAsciiSTL(InputStream inputStream) throws IOException {
        List<String> fileLines = this.readAllLines(inputStream);

        List<Facet> facets = new ArrayList<>();
        List<Top> allTops = new ArrayList<>();
        String solidName = "";
        int i = 0;
        while(i < fileLines.size()) {
            String line = fileLines.get(i);
            if(i == 0) {
                if(!line.startsWith("solid "))
                    Exception.raiseException("Invalid solid format. line 1");

                solidName = line.trim().replace("solid ", "");
                i += 1;
            }else if (i == fileLines.size() - 1) {
                break;
            }else {
                if(!line.startsWith("facet normal ")) {
                    Exception.raiseException("Invalid facet format. line " + i);
                } else {
                    String[] coords = line.replace("facet normal ", "").split(" ");

                    Top normal = new Top(
                            toDouble(coords[0]),
                            toDouble(coords[1]),
                            toDouble(coords[2])
                    );

                    String vertex1 = fileLines.get(i + 2).trim();
                    String vertex2 = fileLines.get(i + 3).trim();
                    String vertex3 = fileLines.get(i + 4).trim();

                    List<Top> vertexs = productTop(i, vertex1, vertex2, vertex3);

                    allTops.addAll(vertexs);

                    Polygon p1 = new Polygon(vertexs.get(0), vertexs.get(1));
                    Polygon p2 = new Polygon(vertexs.get(1), vertexs.get(2));
                    Polygon p3 = new Polygon(vertexs.get(2), vertexs.get(0));

                    List<Polygon> polygons = new ArrayList<>();
                    polygons.add(p1);
                    polygons.add(p2);
                    polygons.add(p3);

                    Facet facet = new Facet(normal, vertexs, polygons);
                    Sync.calculateSurface(facet);
                    facets.add(facet);

                    i += 7;
                }
            }
        }

        return new Polyhedron(solidName, allTops, facets);
    }

    private List<Top> productTop(int i, String vertex1, String vertex2, String vertex3) {
        Top v1 = null, v2 = null, v3 = null;
        if(vertex1.startsWith("vertex ") && vertex2.startsWith("vertex ") && vertex3.startsWith("vertex ")) {
            String[] ver1 = vertex1.replace("vertex ", "").split(" ");
            String[] ver2 = vertex2.replace("vertex ", "").split(" ");
            String[] ver3 = vertex3.replace("vertex ", "").split(" ");
            v1 = new Top(
                    toDouble(ver1[0]),
                    toDouble(ver1[1]),
                    toDouble(ver1[2])
            );
            v2 = new Top(
                    toDouble(ver2[0]),
                    toDouble(ver2[1]),
                    toDouble(ver2[2])
            );
            v3 = new Top(
                    toDouble(ver3[0]),
                    toDouble(ver3[1]),
                    toDouble(ver3[2])
            );
        } else {
            Exception.raiseException("Invalid vertex format " +
                    ". Look between line " + (i + 1) + " and " + (i + 3));
        }
        List<Top> array = new ArrayList<>();
        array.add(v1);
        array.add(v2);
        array.add(v3);
        return array;
    }

    private double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch(java.lang.Exception e) {
            Exception.raiseException("Some value in you file are not double value");
        }
        return 0.0;
    }
}
