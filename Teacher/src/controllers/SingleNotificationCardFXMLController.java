package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SingleNotificationCardFXMLController implements Initializable {
    @FXML
    public Label messageLabel;
    @FXML
    public Label timestampLabel;
    @FXML
    public Label courseLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
