package com.example.demo.network;

import javafx.concurrent.Task;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class Guest extends Node {
    private boolean connect = false;

    public Guest(MeshView mesh) {
        super(mesh);
    }

    public Task<Void> task() {
        return new Task<>() {
            @Override
            protected Void call() throws IOException {
                String hostName = "127.0.0.1";
                int port = 5000;
                System.out.println("Trying connect to server");
                try {
                    socket(new Socket(hostName, port));
                    System.out.println("Connected");
                    com.example.demo.network.State.connect = true;
                    out(new PrintWriter(socket().getOutputStream(), true));

                    process();
                } catch (IOException | ClassNotFoundException | URISyntaxException e) {
                    System.err.println("Host unknown: " + hostName);
                    e.printStackTrace();
                }
                return null;
            }
        };
    }


}
