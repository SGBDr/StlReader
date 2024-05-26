package com.example.demo;

import com.example.demo.Utils.Out;
import com.example.demo.network.ConnectState;
import com.example.demo.network.Node;
import com.example.demo.Utils.Strings;
import com.example.demo.stl_figure.manager.stl.StlReader;
import com.example.demo.stl_figure.model.Facet;
import com.example.demo.stl_figure.model.Polyeder;
import com.example.demo.stl_figure.model.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.demo.Utils.Parser.toDouble;
import static com.example.demo.Utils.Strings.*;
import static javafx.application.Platform.exit;


public class FigureController implements Initializable {
    public static Node node;

    @FXML
    private TextField colorCode;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField fileName;

    @FXML
    public MeshView mesh;

    @FXML
    private AnchorPane scene;

    public static boolean isTranslate = true;

    private double anchorX, anchorY, anchorAngle;

    public MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu(Strings.menu1);
        MenuItem connectMenuItem = new MenuItem(Strings.menuItem1);
        MenuItem exitMenuItem = new MenuItem(Strings.menuItem2);
        fileMenu.getItems().addAll(connectMenuItem, exitMenuItem);
        menuBar.getMenus().add(fileMenu);


        connectMenuItem.setOnAction(event -> {
            String[] options = {ConnectState.GUEST.name(), ConnectState.HOST.name()};
            Alert customDialog = CustomDialog.createCustomDialog(Strings.dialogTitle, Strings.selectOption, Strings.enterIp, options, fileName, colorCode, mesh);
            customDialog.getButtonTypes().add(ButtonType.CANCEL);

            customDialog.show();
        });

        exitMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Strings.exitConfirmation);
            alert.setHeaderText(Strings.wantToExit);
            alert.setContentText(Strings.choose);

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    exit();
                }
            });
        });

        return menuBar;
    }

    public static TriangleMesh generateTriangleMesh(Polyeder polyeder) {
        TriangleMesh mesh = new TriangleMesh();

        int i = -1;
        List<Vertex> aVertex = polyeder.getAllTops();
        for(Facet ignored : polyeder.getFacets()) {
            Vertex t1 = aVertex.get(i + 1);
            Vertex t2 = aVertex.get(i + 2);
            Vertex t3 = aVertex.get(i + 3);

            mesh.getPoints().addAll(
                    t1.getX(), t1.getY(), t1.getZ(),
                    t2.getX(), t2.getY(), t2.getZ(),
                    t3.getX(), t3.getY(), t3.getZ()
            );

            float u1 = (t1.getX() + 1) / 2; // Normalisation
            float v1 = (t1.getY() + 1) / 2; // Normalisation
            float u2 = (t2.getX() + 1) / 2; // Normalisation
            float v2 = (t2.getY() + 1) / 2; // Normalisation
            float u3 = (t3.getX() + 1) / 2; // Normalisation
            float v3 = (t3.getY() + 1) / 2; // Normalisation
            mesh.getTexCoords().addAll(
                    u1, v1,
                    u2, v2,
                    u3, v3
            );

            int numPoints = mesh.getPoints().size() / 3;
            mesh.getFaces().addAll(
                    numPoints - 3, 0, numPoints - 2, 0, numPoints - 1, 0
            );

            i += 3;
        }

        return mesh;
    }

    private void defineListener() {
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngle = mesh.getRotate();
        });

        scene.setOnMouseDragged(event -> {

            if( isTranslate ) {
                double aX = event.getX(), aY = event.getY();

                mesh.setLayoutX(aX);
                mesh.setLayoutY(aY);

                if(node != null)
                    node.changePosition(aX + Strings.comma + aY);
            } else {
                double rot = -1;
                if(mesh.getRotationAxis().equals(Rotate.X_AXIS)) {
                    rot = anchorAngle + anchorY - event.getSceneY();
                } else if(mesh.getRotationAxis().equals(Rotate.Y_AXIS)) {
                    rot = anchorAngle + anchorX - event.getSceneX();
                } else {
                    rot = anchorAngle + (anchorY + anchorX - event.getSceneY() - event.getSceneX());
                }

                if(rot != -1)
                    mesh.setRotate(rot);

                if(node != null && rot != -1)
                    node.changeRotation(rot + Strings.empty);

            }
        });
    }

    @FXML
    void translate(ActionEvent event) {
        isTranslate = true;

        if(node != null)
            node.translate();
    }

    @FXML
    void rotate(ActionEvent event) {
        isTranslate = false;

        if(node != null)
            node.rotate();
    }

    @FXML
    void applyColorCode(ActionEvent event) {
        String[] colorComponents = colorCode.getText().trim().split(Strings.comma);

        if(colorComponents.length < 3 || colorComponents.length > 4)
            Out.exception(Strings.invalidColor);

        if(colorComponents.length == 3) {
            mesh.setMaterial(new PhongMaterial(new Color(
                    toDouble(colorComponents[0]),
                    toDouble(colorComponents[1]),
                    toDouble(colorComponents[2]),
                    0.0
            )));
            if (node != null)
                node.changeColor(colorCode.getText().trim() + END_OF_COLOR);
        } else {
            mesh.setMaterial(new PhongMaterial(new Color(
                    toDouble(colorComponents[0]),
                    toDouble(colorComponents[1]),
                    toDouble(colorComponents[2]),
                    toDouble(colorComponents[3])
            )));
            if (node != null)
                node.changeColor(colorCode.getText().trim());
        }
    }

    @FXML
    void loadNewStl(ActionEvent event) throws IOException {
        StlReader s = new StlReader();

        Polyeder polyeder = s.readPolyedre(fileName.getText());

        if(node != null)
            node.changeData(polyeder);

        this.fileName.setText(polyeder.getName());
        mesh.setMesh(generateTriangleMesh(polyeder));
    }

    @FXML
    void rotationAxeX(ActionEvent event) {
        mesh.setRotationAxis(Rotate.X_AXIS);
        if(node != null)
            node.changeAxe(AXE_X);
    }

    @FXML
    void rotationAxeY(ActionEvent event) {
        mesh.setRotationAxis(Rotate.Y_AXIS);
        if(node != null)
            node.changeAxe(AXE_Y);
    }

    @FXML
    void rotationAllAxes(ActionEvent event) {
        mesh.setRotationAxis(new Point3D(150, 50, 100));
        if(node != null)
            node.changePoint(ROTATION_POINT);
    }

    @FXML
    void rotationAxeZ(ActionEvent event) {
        mesh.setRotationAxis(Rotate.Z_AXIS);
        if(node != null)
            node.changeAxe(AXE_Z);
    }

    @FXML
    void setBlueColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(0, 0, 1, 1)));
        if(node != null)
            node.changeColor(COLOR_BLUE);
    }

    @FXML
    void setGreenColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(0, 1, 0, 1)));
        if(node != null)
            node.changeColor(COLOR_GREEN);
    }

    @FXML
    void setRedColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(1, 0, 0, 1)));
        if(node != null)
            node.changeColor(COLOR_RED);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mesh.setBlendMode(BlendMode.COLOR_BURN);
        mesh.setMaterial(new PhongMaterial(Color.DARKGREEN));

        mesh.setCullFace(CullFace.BACK);
        mesh.setRotationAxis(Rotate.X_AXIS);

        borderPane.setTop(createMenu());

        defineListener();
    }
}
