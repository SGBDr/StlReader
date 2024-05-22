package com.example.demo;

import com.example.demo.Utils.Strings;
import com.example.demo.network.ConnectState;
import com.example.demo.network.Guest;
import com.example.demo.network.Host;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.MeshView;

public class CustomDialog {

    public static Alert createCustomDialog(String title, String headerText, String contentText,
                                           String[] options, TextField fileName, TextField colorCode, MeshView mesh) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options);
        TextField ipTextField = new TextField();
        Button validateButton = new Button(Strings.validateButton);
        Label errorLabel = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label(Strings.selectOption), 0, 0);
        gridPane.add(comboBox, 1, 0);
        gridPane.add(new Label(Strings.enterIp), 0, 1);
        gridPane.add(ipTextField, 1, 1);
        gridPane.add(validateButton, 0, 2);
        gridPane.add(errorLabel, 1, 2);

        alert.getDialogPane().setContent(gridPane);

        validateButton.setOnAction(event -> {
            String response = comboBox.getValue();
            if(response != null) {
                if(response.equals(ConnectState.HOST.name())) {
                    FigureController.node = new Host(mesh, colorCode, fileName, errorLabel).runThread(ipTextField.getText());
                } else if(response.equals(ConnectState.GUEST.name())) {
                    FigureController.node = new Guest(mesh, colorCode, fileName, errorLabel).runThread(ipTextField.getText());
                }
            }
        });

        return alert;
    }
}
