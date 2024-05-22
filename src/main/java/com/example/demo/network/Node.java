package com.example.demo.network;

import com.example.demo.FigureController;
import com.example.demo.stl_figure.model.Polyeder;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

import static com.example.demo.Utils.Strings.*;

public abstract class Node {
    private final MeshView mesh;
    private final TextField color;
    private final TextField fileName;
    private final Label errorLabel;
    private PrintWriter out;
    private Socket socket;

    private boolean data = false;

    protected Node(MeshView mesh, TextField color, TextField fileName, Label errorLabel) {
        this.mesh = mesh;
        this.color = color;
        this.fileName = fileName;
        this.errorLabel = errorLabel;
    }

    protected synchronized void process() throws IOException, ClassNotFoundException, URISyntaxException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket().getInputStream()));
        while (State.connect) {

            if(data) {
                ObjectInputStream inData = new ObjectInputStream(socket.getInputStream());
                Polyeder obj = (Polyeder) inData.readObject();
                mesh.setMesh(FigureController.generateTriangleMesh(obj));
                data = false;
            }else {
                String inputLine = in.readLine();

                if(inputLine != null && !inputLine.isEmpty()) {

                    if(inputLine.startsWith(ROTATION)) {
                        String data = inputLine.replace(ROTATION, empty);
                        Platform.runLater(() -> mesh.setRotate(Double.parseDouble(data)));
                    }
                    else if(inputLine.startsWith(ROTATE)){
                        FigureController.isTranslate = false;
                    }
                    else if(inputLine.startsWith(TRANSLATE)){
                        FigureController.isTranslate = true;
                    }
                    else if(inputLine.startsWith(POSITION)) {
                        inputLine = inputLine.replace(POSITION, empty);
                        String[] data = inputLine.split(comma);

                        Platform.runLater(() -> {
                            mesh.setLayoutX(Double.parseDouble(data[0]));
                            mesh.setLayoutY(Double.parseDouble(data[1]));
                        });
                    }
                    else if(inputLine.startsWith(COLOR)) {
                        inputLine = inputLine.replace(COLOR, empty);
                        String[] data = inputLine.split(comma);
                        mesh.setMaterial(
                                new PhongMaterial(
                                        new Color(
                                                Double.parseDouble(data[0]),
                                                Double.parseDouble(data[1]),
                                                Double.parseDouble(data[2]),
                                                Double.parseDouble(data[3]))
                                )
                        );
                        color.setText(inputLine);
                    }
                    else if(inputLine.startsWith(AXE)) {
                        inputLine = inputLine.replace(AXE, empty);

                        switch (inputLine) {
                            case AXE_X -> mesh.setRotationAxis(Rotate.X_AXIS);
                            case AXE_Y -> mesh.setRotationAxis(Rotate.Y_AXIS);
                            case AXE_Z -> mesh.setRotationAxis(Rotate.Z_AXIS);
                        }
                    }
                    else if(inputLine.startsWith(POINT)) {
                        inputLine = inputLine.replace(POINT, empty);
                        String[] data = inputLine.split(comma);

                        Platform.runLater(() -> mesh.setRotationAxis(
                                new Point3D(
                                        Double.parseDouble(data[0]),
                                        Double.parseDouble(data[1]),
                                        Double.parseDouble(data[2])
                                )
                        ));

                    }
                    else if(inputLine.startsWith(DATA)) {
                        data = true;
                        String name = inputLine.replace(DATA, empty);
                        fileName.setText(name);
                    }
                }
            }

        }
    }

    public void changeData(Polyeder polyeder) throws IOException {
        out.println(DATA + polyeder.getName());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(polyeder);
        out.flush();
    }

    protected void out(PrintWriter out) {this.out = out;}

    protected void socket(Socket socket) {this.socket = socket;}

    protected Socket socket() {return socket;}

    public void changeRotation(String rotation) {
        out.println(ROTATION + rotation);
    }

    public void changePosition(String position) {
        out.println(POSITION + position);
    }

    public void changeAxe(String axe) {
        out.println(AXE + axe);
    }

    public void rotate() {
        out.println(ROTATE);
    }

    public void translate() {
        out.println(TRANSLATE);
    }

    public void changePoint(String point) {
        out.println(POINT + point);
    }

    public void changeColor(String color) {
        out.println(COLOR + color);
    }

    public Node runThread(String ip) {
        new Thread(task()).start();
        return this;
    }

    protected void displayText(String error) {
        Platform.runLater(() -> errorLabel.setText(error));
    }

    public abstract Task<Void> task();
}
