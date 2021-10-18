package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import request.TeacherLoginRequest;
import response.TeacherLoginResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherLoginController implements Initializable {
    @FXML
    public Button loginButton;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField usernameField;
    @FXML
    public Label signinLabel;
    @FXML
    public Hyperlink signupLink;

    public void login(ActionEvent actionEvent) {
        System.out.println("Creating a request object");
        TeacherLoginRequest request=new TeacherLoginRequest(usernameField.getText(),passwordField.getText());
        Main.sendRequest(request);
        System.out.println("Request.Request Sent");
        TeacherLoginResponse response= (TeacherLoginResponse) Main.receiveResponse();
        if (response != null && response.getFirstName() == null) {
            System.out.println("Wrong Info");
        }
        else {
            assert response != null;
            if(response==null) System.out.println("null response");
            System.out.println("Teacher ID is "+response.getTeacherID());
            FXMLLoader homepageLoader= new FXMLLoader(getClass().getResource("../views/TeacherHomeView2.fxml"));
            Stage currentStage=(Stage)loginButton.getScene().getWindow();
            Scene scene=null;
            try {
                scene=new Scene(homepageLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentStage.setScene(scene);
            currentStage.setTitle("Welcome");
            Main.setTeacherId(response.getTeacherID());
            TeacherHomeController controller = homepageLoader.getController();
            controller.callFirst();
        }
    }

    public void switchToSignup(ActionEvent actionEvent) {
        FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("../views/TeacherRegisterView.fxml"));
        Scene scene = null;
        Stage stage = (Stage) signupLink.getScene().getWindow();
        try {
            scene = new Scene(registerLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Sign Up");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
