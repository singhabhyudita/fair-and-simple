package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SingleImageChatCardFXMLController implements Initializable {
    @FXML
    public ImageView imageView;
    @FXML
    public Label timestampLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public HBox nameHBox;
    @FXML
    public VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
