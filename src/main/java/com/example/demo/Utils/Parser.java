package com.example.demo.Utils;

import com.example.demo.stl_figure.model.Vertex;

import java.util.List;

import static com.example.demo.Utils.Strings.empty;
import static com.example.demo.Utils.Strings.space;

public class Parser {
    public static boolean isBinary(String firstLine) {
        return firstLine.startsWith(Strings.START_OF_BIN);
    }

    public static boolean isAscii(String firstLine) {
        return firstLine.startsWith(Strings.START_OF_ASCII);
    }

    public static Vertex getCoord(String[] coord) {
        return new Vertex(
                toFloat(coord[0]),
                toFloat(coord[1]),
                toFloat(coord[2])
        );
    }

    public static List<Vertex> createVertex(String v1, String v2, String v3) {
        if(!v1.startsWith(Strings.START_OF_VERTEX) || !v2.startsWith(Strings.START_OF_VERTEX) || !v3.startsWith(Strings.START_OF_VERTEX)) {
            Exception.raise(Strings.WRONG_VERTEX_FORMAT);
        }

        String[] vCoord1 = v1.replace(Strings.START_OF_VERTEX, empty).split(space);
        String[] vCoord2 = v2.replace(Strings.START_OF_VERTEX, empty).split(space);
        String[] vCoord3 = v3.replace(Strings.START_OF_VERTEX, empty).split(space);

        return List.of(getCoord(vCoord1), getCoord(vCoord2), getCoord(vCoord3));
    }

    public static float toFloat(String coord) {
        try{
            return Float.parseFloat(coord);
        }catch(NumberFormatException e){
            Exception.raise(Strings.NOT_A_NUMBER);
        }

        return 0.0f;
    }
}
