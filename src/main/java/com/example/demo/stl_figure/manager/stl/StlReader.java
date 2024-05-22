package com.example.demo.stl_figure.manager.stl;


import com.example.demo.Utils.Strings;
import com.example.demo.Utils.Out;
import com.example.demo.Utils.Exception;
import com.example.demo.stl_figure.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Utils.Parser.*;
import static com.example.demo.Utils.Strings.*;

public class StlReader {

    private Polyeder readAscii(List<String> lines) {
        String name = "";
        List<Facet> facets = new ArrayList<>();

        int i = 0;
        while(i < lines.size()) {
            String line = lines.get(i);

            if(i == lines.size() - 1)
                break;
            else if(i == 0) {
                if(line.startsWith(Strings.START_OF_ASCII))
                    name = line.replace(Strings.START_OF_ASCII, empty);
                else
                    Exception.raise(Strings.errorLine(i + 1));
                i++;
            }else {
                if(line.startsWith(Strings.START_OF_FACET)) {
                    line = line.replace(Strings.START_OF_FACET, empty);
                    String[] normalCoord = line.split(space);
                    Vertex normal = getCoord(normalCoord);

                    String vertex1 = lines.get(i + 2);
                    String vertex2 = lines.get(i + 3);
                    String vertex3 = lines.get(i + 4);

                    List<Vertex> vertices = createVertex(vertex1, vertex2, vertex3);
                    List<Edge> edges = List.of(
                            new Edge(vertices.get(0), vertices.get(1)),
                            new Edge(vertices.get(1), vertices.get(2)),
                            new Edge(vertices.get(2), vertices.get(0))
                    );
                    Facet facet = new Facet(edges, new Normal(normal));

                    Synchroniser.calculateSurface(facet); // for the part 3, Async surface calculation

                    facets.add(facet);
                    i += 7;
                } else
                    Exception.raise(Strings.errorLine(i + 1));
            }
        }

        return new Polyeder(name, facets);
    }


    private Polyeder readBin(List<String> lines) {
        List<Facet> facets = new ArrayList<>();

        int i = 3;
        while(i < lines.size()) {
            String line = lines.get(i);

            if(i == lines.size() - 1)
                break;
            else if(line.startsWith(Strings.START_OF_TRIANGLE)) {
                String normalString = lines.get(i + 2).replace(Strings.START_OF_TRIANGLE, empty);
                Vertex normal = getCoord(normalString.split(space));

                String vertex1 = lines.get(i + 4).replace(Strings.START_OF_TRIANGLE, empty);
                String vertex2 = lines.get(i + 6).replace(Strings.START_OF_TRIANGLE, empty);
                String vertex3 = lines.get(i + 8).replace(Strings.START_OF_TRIANGLE, empty);

                List<Vertex> vertices = List.of(
                        getCoord(vertex1.split(space)), getCoord(vertex2.split(space)), getCoord(vertex3.split(" "))
                );

                List<Edge> edges = List.of(
                        new Edge(vertices.get(0), vertices.get(1)),
                        new Edge(vertices.get(1), vertices.get(2)),
                        new Edge(vertices.get(2), vertices.get(0))
                );
                Facet facet = new Facet(edges, new Normal(normal));

                Synchroniser.calculateSurface(facet); // for the part 3, Async surface calculation

                facets.add(facet);

                i += 11;
            }else
                Exception.raise(Strings.errorLine(i + 1));
        }

        return new Polyeder(Strings.DEFAULT_NAME_FOR_BIN_STL, facets);
    }


    public Polyeder readPolyedre(String filename) throws IOException {
        List<String> lines = readAllLines(filename);
        String firstLine = lines.get(0);

        if(isAscii(firstLine)) {
            return readAscii(lines);
        } else if (isBinary(firstLine)) {
            return readBin(lines);
        }

        Exception.raise(Strings.UNKNOWN_FILE);
        return null;
    }


    private List<String> readAllLines(String filename) {
        List<String> listLine = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Strings.SRC_MAIN_RESOURCES + filename));
            String line;
            while ((line = reader.readLine()) != null) {
                if(!line.isEmpty())
                    listLine.add(line.trim());
            }
            reader.close();
        } catch (IOException e) {
            Out.print(e.getMessage());
        }
        return listLine;
    }
}
