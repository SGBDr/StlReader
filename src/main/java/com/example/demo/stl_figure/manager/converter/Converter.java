package com.example.demo.stl_figure.manager.converter;

import com.example.demo.HelloApplication;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Converter {
    public static void convertBinaryToAsciiSTL(String inputFileName, String outputFileName) throws IOException {
        try{
            final InputStream inputStream = new FileInputStream(new File(HelloApplication.class.getResource(inputFileName).toURI()));
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final BufferedWriter writer = new BufferedWriter(new FileWriter(new File(HelloApplication.class.getResource(outputFileName).toURI())));
            // Write ASCII header
            writer.write("solid converted\n");

            // Skip the header bytes
            inputStream.skip(80);

            // Read the number of triangles
            byte[] buffer = new byte[4];
            inputStream.read(buffer);
            int numTriangles = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();

            // Process each triangle
            for (int i = 0; i < numTriangles; i++) {
                // Read normal vector
                float[] normal = readFloatArray(inputStream, 3);
                writer.write(String.format("  facet normal %f %f %f\n", normal[0], normal[1], normal[2]));

                // Read vertices
                for (int j = 0; j < 3; j++) {
                    float[] vertex = readFloatArray(inputStream, 3);
                    writer.write(String.format("    outer loop\n"));
                    writer.write(String.format("      vertex %f %f %f\n", vertex[0], vertex[1], vertex[2]));
                }
            }
                // Write end of facet
                writer.write(String.format("    endloop\n"));
                writer.write(String.format("  endfacet\n"));
        // Write end of solid
            writer.write("endsolid converted\n");
            System.out.println("okok");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static float[] readFloatArray(InputStream inputStream, int length) throws IOException {
        float[] array = new float[length];
        byte[] buffer = new byte[4];
        for (int i = 0; i < length; i++) {
            inputStream.read(buffer);
            array[i] = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        }
        return array;
    }
}
