package Controller;

import Response.LoginResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {
    @FXML
    public AnchorPane homePagePane;
    @FXML
    public Label welcomeLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public Hyperlink logoutLink;

    private Scene homeScene;
    public void initData(Scene homeScene, LoginResponse loginResponse){
        String firstName = loginResponse.getFirstName();
        String lastName = loginResponse.getLastName();
        this.homeScene=homeScene;
        nameLabel.setText(firstName +" "+ lastName);
    }
    public void logout(ActionEvent actionEvent) {
        FXMLLoader logoutLoader= new FXMLLoader(getClass().getResource("../FXML/Login.fxml"));
        Stage stage=(Stage)logoutLink.getScene().getWindow();
        Scene scene=null;
        try {
            scene=new Scene(logoutLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Sign In");
    }
}
