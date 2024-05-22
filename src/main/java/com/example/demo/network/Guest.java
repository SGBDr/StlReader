package com.example.demo.network;

import com.example.demo.Utils.Out;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.MeshView;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

import static com.example.demo.Utils.Strings.*;

public class Guest extends Node {

    public Guest(MeshView mesh, TextField filename, TextField colorCode, Label errorLabel) {
        super(mesh, filename, colorCode, errorLabel);
    }

    public Task<Void> task() {
        return new Task<>() {
            @Override
            protected Void call() {
                String hostName = "127.0.0.1";
                displayText(TRYING);
                try {
                    socket(new Socket(hostName, DEFAULT_PORT));
                    displayText(CONNECTED);
                    com.example.demo.network.State.connect = true;
                    out(new PrintWriter(socket().getOutputStream(), true));

                    process();
                } catch (IOException | ClassNotFoundException | URISyntaxException e) {
                    displayText(UNKNOWN_HOST + hostName);
                    Out.print(e.getMessage());
                    com.example.demo.network.State.connect = false;
                }
                return null;
            }
        };
    }


}
