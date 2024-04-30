package com.example.demo;

import com.example.demo.network.ConnectState;
import com.example.demo.network.Guest;
import com.example.demo.network.Host;
import com.example.demo.network.Node;
import com.example.demo.stl_figure.exception.Exception;
import com.example.demo.stl_figure.manager.stl.STLReaderImpl;
import com.example.demo.stl_figure.model.Facet;
import com.example.demo.stl_figure.model.Polyhedron;
import com.example.demo.stl_figure.model.Top;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
        Menu fileMenu = new Menu("File");
        MenuItem connectMenuItem = new MenuItem("Connect");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(connectMenuItem, exitMenuItem);
        menuBar.getMenus().add(fileMenu);


        connectMenuItem.setOnAction(event -> {
            String[] options = {ConnectState.GUEST.name(), ConnectState.HOST.name()};
            Alert customDialog = createCustomDialog("Connect Hub", "Select Option", "Enter IP Address:", options);
            customDialog.getButtonTypes().add(ButtonType.CANCEL);

            customDialog.show();
        });

        exitMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Confirmation");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("Click OK to exit or Cancel to stay.");

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    exit();
                }
            });
        });

        return menuBar;
    }

    public static TriangleMesh generateTriangleMesh(Polyhedron polyhedron) throws URISyntaxException, IOException {
        TriangleMesh mesh = new TriangleMesh();

        int i = -1;
        List<Top> aTop = polyhedron.getAllTops();
        for(Facet facet : polyhedron.getFacets()) {
            Top t1 = aTop.get(i + 1);
            Top t2 = aTop.get(i + 2);
            Top t3 = aTop.get(i + 3);

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
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                anchorAngle = mesh.getRotate();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {

                if( isTranslate ) {
                    double aX = event.getX(), aY = event.getY();

                    mesh.setLayoutX(aX);
                    mesh.setLayoutY(aY);

                    if(node != null)
                        node.changePosition(aX + ";" + aY);
                } else {
                    double rot = -1;
                    if(mesh.getRotationAxis().equals(Rotate.X_AXIS)) {
                        rot = anchorAngle + anchorY - event.getSceneY();
                        mesh.setRotate(rot);
                    } else if(mesh.getRotationAxis().equals(Rotate.Y_AXIS)) {
                        rot = anchorAngle + anchorX - event.getSceneX();
                        mesh.setRotate(rot);
                    } else {
                        rot = anchorAngle + (anchorY + anchorX - event.getSceneY() - event.getSceneX());
                        mesh.setRotate(rot);
                    }

                    if(node != null && rot != -1)
                        node.changeRotation(rot + "");

                }
            }
        });
    }

    @FXML
    void translate(ActionEvent event) {
        this.isTranslate = true;

        if(node != null)
            node.translate();
    }

    @FXML
    void rotate(ActionEvent event) {
        this.isTranslate = false;

        if(node != null)
            node.rotate();
    }

    @FXML
    void applyColorCode(ActionEvent event) {
        String[] colorComponents = colorCode.getText().trim().split(";");

        if(colorComponents.length < 3 || colorComponents.length > 4)
            Exception.raiseException("Invalid color code, exemple .123;.23;.34 or .123;.34;.34;0.2");

        if(colorComponents.length == 3) {
            mesh.setMaterial(new PhongMaterial(new Color(
                    toDouble(colorComponents[0]),
                    toDouble(colorComponents[1]),
                    toDouble(colorComponents[2]),
                    0.0
            )));
            if (node != null)
                node.changeColor(colorCode.getText().trim() + ";1");
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

    private double toDouble(String value) {
        try{
            return Double.parseDouble(value);
        }catch(java.lang.Exception e) {
            Exception.raiseException("Invalid color component. one of your color component are not double");
        }
        return .0;
    }

    @FXML
    void loadNewStl(ActionEvent event) throws URISyntaxException, IOException {
        STLReaderImpl s = new STLReaderImpl();

        Polyhedron polyhedron = s.readSTLFile(fileName.getText());

        if(node != null)
            node.changeData(polyhedron);
        mesh.setMesh(generateTriangleMesh(polyhedron));
    }

    @FXML
    void rotationAxeX(ActionEvent event) {
        mesh.setRotationAxis(Rotate.X_AXIS);
        if(node != null)
            node.changeAxe("X");
    }

    @FXML
    void rotationAxeY(ActionEvent event) {
        mesh.setRotationAxis(Rotate.Y_AXIS);
        if(node != null)
            node.changeAxe("Y");
    }

    @FXML
    void rotationAllAxes(ActionEvent event) {
        mesh.setRotationAxis(new Point3D(150, 50, 100));
        if(node != null)
            node.changePoint("150;50;100");
    }

    @FXML
    void rotationAxeZ(ActionEvent event) {
        mesh.setRotationAxis(Rotate.Z_AXIS);
        if(node != null)
            node.changeAxe("Z");
    }

    @FXML
    void setBlueColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(0, 0, 1, 1)));
        if(node != null)
            node.changeColor("0;0;1;1");
    }

    @FXML
    void setGreenColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(0, 1, 0, 1)));
        if(node != null)
            node.changeColor("0;1;0;1");
    }

    @FXML
    void setRedColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(1, 0, 0, 1)));
        if(node != null)
            node.changeColor("1;0;0;1");
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

    public Alert createCustomDialog(String title, String headerText, String contentText, String[] options) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options);
        TextField ipTextField = new TextField();
        Button validateButton = new Button("Validate");
        Label errorLabel = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label("Select Option:"), 0, 0);
        gridPane.add(comboBox, 1, 0);
        gridPane.add(new Label("Enter IP:"), 0, 1);
        gridPane.add(ipTextField, 1, 1);
        gridPane.add(validateButton, 0, 2);
        gridPane.add(errorLabel, 1, 2);

        alert.getDialogPane().setContent(gridPane);

        validateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String response = comboBox.getValue();
                System.out.println(response);
                if(response != null) {
                    if(response == ConnectState.HOST.name()) {
                        FigureController.node = new Host(mesh).runThread(ipTextField.getText());
                    } else if(response == ConnectState.GUEST.name()) {
                        FigureController.node = new Guest(mesh).runThread(ipTextField.getText());
                    }
                }
            }
        });

        return alert;
    }
}
