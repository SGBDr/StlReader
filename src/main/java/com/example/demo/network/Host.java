package com.example.demo.network;

import javafx.concurrent.Task;
import javafx.scene.shape.MeshView;

import java.io.*;
import java.net.ServerSocket;
import java.net.URISyntaxException;

public class Host extends Node {
    private MeshView mesh;

    public Host(MeshView mesh) {
        super(mesh);
        this.mesh = mesh;
    }

    public Node runThread(String ip) {
        new Thread(task()).start();
        return this;
    }

    public synchronized Task<Void> task() {
        return new Task<>() {
            @Override
            protected Void call() {
                int port = 5000;
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    System.out.println("Waiting for connexion");
                    try {
                        socket(serverSocket.accept());
                        System.out.println("Someone connected");
                        com.example.demo.network.State.connect = true;
                        out(new PrintWriter(socket().getOutputStream(), true));

                        process();
                    }
                    catch (IOException | ClassNotFoundException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
