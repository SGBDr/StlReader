package com.example.demo.network;

import com.example.demo.FigureController;
import com.example.demo.stl_figure.model.Polyhedron;
import javafx.concurrent.Task;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public abstract class Node {
    private final MeshView mesh;
    private PrintWriter out;
    private Socket socket;

    private boolean data = false;

    private float time = System.currentTimeMillis();

    protected Node(MeshView mesh) {
        this.mesh = mesh;
    }

    protected synchronized void process() throws IOException, ClassNotFoundException, URISyntaxException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket().getInputStream()));
        while (true) {

            if(data) {
                ObjectInputStream inData = new ObjectInputStream(socket.getInputStream());
                Polyhedron obj = (Polyhedron) inData.readObject();
                mesh.setMesh(FigureController.generateTriangleMesh(obj));
                data = false;
            }else {
                String inputLine = in.readLine();

                if(inputLine != null && inputLine != "") {

                    if(inputLine.startsWith("ROTATION")) {
                        String data = inputLine.replace("ROTATION ", "");
                        mesh.setRotate(Double.parseDouble(data));
                    }
                    else if(inputLine.startsWith("ROTATE")){
                        FigureController.isTranslate = false;
                    }
                    else if(inputLine.startsWith("TRANSLATE")){
                        FigureController.isTranslate = true;
                    }
                    else if(inputLine.startsWith("POSITION")) {
                        inputLine = inputLine.replace("POSITION ", "");
                        String[] data = inputLine.split(";");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mesh.setLayoutX(Double.parseDouble(data[0]));
                                mesh.setLayoutY(Double.parseDouble(data[1]));
                            }
                        }).start();
                    }
                    else if(inputLine.startsWith("COLOR")) {
                        inputLine = inputLine.replace("COLOR ", "");
                        String[] data = inputLine.split(";");
                        mesh.setMaterial(
                                new PhongMaterial(
                                        new Color(
                                                Double.parseDouble(data[0]),
                                                Double.parseDouble(data[1]),
                                                Double.parseDouble(data[2]),
                                                Double.parseDouble(data[3]))
                                )
                        );
                    }
                    else if(inputLine.startsWith("AXIS")) {
                        inputLine = inputLine.replace("AXIS ", "");

                        if(inputLine.equals("X"))
                            mesh.setRotationAxis(Rotate.X_AXIS);
                        else if(inputLine.equals("Y"))
                            mesh.setRotationAxis(Rotate.Y_AXIS);
                        else if(inputLine.equals("Z"))
                            mesh.setRotationAxis(Rotate.Z_AXIS);
                    }
                    else if(inputLine.startsWith("POINT")) {
                        inputLine = inputLine.replace("POINT ", "");
                        String[] data = inputLine.split(";");
                        mesh.setRotationAxis(
                                new Point3D(
                                        Double.parseDouble(data[0]),
                                        Double.parseDouble(data[1]),
                                        Double.parseDouble(data[2])
                                )
                        );

                    }
                    else if(inputLine.startsWith("DATA")) {
                        data = true;
                    }
                }
            }

        }
    }

    public void changeData(Polyhedron polyhedron) throws IOException {
        out.println("DATA ");
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(polyhedron);
        out.flush();
    }

    protected void out(PrintWriter out) {this.out = out;}

    protected void socket(Socket socket) {this.socket = socket;}
    protected Socket socket() {return socket;}

    public void changeRotation(String rotation) {
        out.println("ROTATION " + rotation);

    }

    public void changePosition(String position) {
        out.println("POSITION " + position);
    }
    public void changeAxe(String axe) {
        out.println("AXIS " + axe);
    }

    public void rotate() {
        out.println("ROTATE");
    }
    public void translate() {
        out.println("TRANSLATE");
    }

    public void changePoint(String point) {
        out.println("POINT " + point);
    }

    public void changeColor(String color) {
        out.println("COLOR " + color);
    }

    public Node runThread(String ip) {
        new Thread(task()).start();
        return this;
    }

    public abstract Task<Void> task();
}
