package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SingleChatCardFXMLController implements Initializable {
    @FXML
    public Label messageLabel;
    @FXML
    public Label timestampLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public HBox nameHBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
