package com.example.demo;

import com.example.demo.stl_figure.Constant;
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
        Menu fileMenu = new Menu(Constant.menu1);
        MenuItem connectMenuItem = new MenuItem(Constant.menuItem1);
        MenuItem exitMenuItem = new MenuItem(Constant.menuItem2);
        fileMenu.getItems().addAll(connectMenuItem, exitMenuItem);
        menuBar.getMenus().add(fileMenu);


        connectMenuItem.setOnAction(event -> {
            String[] options = {};
            Alert customDialog = createCustomDialog(Constant.dialogTitle, Constant.selectOption, Constant.enterIp, options);
            customDialog.getButtonTypes().add(ButtonType.CANCEL);

            customDialog.show();
        });

        exitMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Constant.exitConfirmation);
            alert.setHeaderText(Constant.wantToExit);
            alert.setContentText(Constant.choose);

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    exit();
                }
            });
        });

        return menuBar;
    }

    public static TriangleMesh generateTriangleMesh(Polyhedron polyhedron) {
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

                }
            }
        });
    }

    @FXML
    void translate(ActionEvent event) {
        this.isTranslate = true;
    }

    @FXML
    void rotate(ActionEvent event) {
        this.isTranslate = false;
    }

    @FXML
    void applyColorCode(ActionEvent event) {
        String[] colorComponents = colorCode.getText().trim().split(Constant.comma);

        if(colorComponents.length < 3 || colorComponents.length > 4)
            Exception.raiseException(Constant.invalidColor);

        if(colorComponents.length == 3) {
            mesh.setMaterial(new PhongMaterial(new Color(
                    toDouble(colorComponents[0]),
                    toDouble(colorComponents[1]),
                    toDouble(colorComponents[2]),
                    0.0
            )));
        } else {
            mesh.setMaterial(new PhongMaterial(new Color(
                    toDouble(colorComponents[0]),
                    toDouble(colorComponents[1]),
                    toDouble(colorComponents[2]),
                    toDouble(colorComponents[3])
            )));
        }
    }

    private double toDouble(String value) {
        try{
            return Double.parseDouble(value);
        }catch(java.lang.Exception e) {
            Exception.raiseException(Constant.invalidColorComponent);
        }
        return .0;
    }

    @FXML
    void loadNewStl(ActionEvent event) throws URISyntaxException, IOException {
        STLReaderImpl s = new STLReaderImpl();

        Polyhedron polyhedron = s.readSTLFile(fileName.getText());
        mesh.setMesh(generateTriangleMesh(polyhedron));
    }

    @FXML
    void rotationAxeX(ActionEvent event) {
        mesh.setRotationAxis(Rotate.X_AXIS);
    }

    @FXML
    void rotationAxeY(ActionEvent event) {
        mesh.setRotationAxis(Rotate.Y_AXIS);
    }

    @FXML
    void rotationAllAxes(ActionEvent event) {
        mesh.setRotationAxis(new Point3D(150, 50, 100));
    }

    @FXML
    void rotationAxeZ(ActionEvent event) {
        mesh.setRotationAxis(Rotate.Z_AXIS);
    }

    @FXML
    void setBlueColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(0, 0, 1, 1)));
    }

    @FXML
    void setGreenColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(0, 1, 0, 1)));
    }

    @FXML
    void setRedColor(ActionEvent event) {
        mesh.setMaterial(new PhongMaterial(new Color(1, 0, 0, 1)));
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
        Button validateButton = new Button(Constant.validateButton);
        Label errorLabel = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label(Constant.selectOption), 0, 0);
        gridPane.add(comboBox, 1, 0);
        gridPane.add(new Label(Constant.enterIp), 0, 1);
        gridPane.add(ipTextField, 1, 1);
        gridPane.add(validateButton, 0, 2);
        gridPane.add(errorLabel, 1, 2);

        alert.getDialogPane().setContent(gridPane);

        validateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // code that handle the click on validate button
            }
        });

        return alert;
    }
}
