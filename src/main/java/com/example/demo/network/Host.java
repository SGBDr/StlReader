package com.example.demo.network;

import com.example.demo.Utils.Out;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.MeshView;

import java.io.*;
import java.net.ServerSocket;
import java.net.URISyntaxException;

import static com.example.demo.Utils.Strings.*;

public class Host extends Node {

    public Host(MeshView mesh, TextField filename, TextField colorCode, Label errorLabel) {
        super(mesh, filename, colorCode, errorLabel);

    }

    public Node runThread(String ip) {
        new Thread(task()).start();
        return this;
    }

    public synchronized Task<Void> task() {
        return new Task<>() {
            @Override
            protected Void call() {
                try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
                    displayText(WAITING);
                    try {
                        socket(serverSocket.accept());
                        com.example.demo.network.State.connect = true;
                        displayText(SOMEONE_CONNECTED);
                        out(new PrintWriter(socket().getOutputStream(), true));

                        process();
                    }
                    catch (IOException | ClassNotFoundException | URISyntaxException e) {
                        Out.print(e.getMessage());
                        displayText(CONNEXION_FAIL);
                        com.example.demo.network.State.connect = false;
                    }
                } catch (IOException e) {
                    Out.print(e.getMessage());
                    displayText(CONNEXION_FAIL);
                    com.example.demo.network.State.connect = false;
                }
                return null;
            }
        };
    }
}
