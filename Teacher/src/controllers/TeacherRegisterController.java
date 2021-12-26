package controllers;

import main.Main;
import request.TeacherRegisterRequest;
import response.TeacherRegisterResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.HashUtil;

import java.io.IOException;

public class TeacherRegisterController {
    @FXML
    public Button registerButton;
    @FXML
    public Hyperlink loginLink;
    @FXML
    public Label signupLabel;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField teacherIDField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public TextField emailIDField;
    @FXML
    public Label matchLabel;

    public void first(){
        firstNameField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,15}") ? c : null));
        lastNameField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,15}") ? c : null));
        emailIDField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,50}") ? c : null));
        teacherIDField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{4}") ? c : null));
    }

    public void register(ActionEvent actionEvent) {
        FXMLLoader loginLoader=new FXMLLoader(getClass().getResource("../views/TeacherLoginView.fxml"));
        if(passwordField.getText().length() != 0
                && passwordField.getText().equals(confirmPasswordField.getText())
                && firstNameField.getText().length() != 0
                && lastNameField.getText().length() != 0
                && emailIDField.getText().length() != 0) {
            TeacherRegisterRequest registerRequest=new TeacherRegisterRequest(firstNameField.getText(),lastNameField.getText(),emailIDField.getText(),
                    HashUtil.getMd5(passwordField.getText()),teacherIDField.getText());
            Main.sendRequest(registerRequest);
            System.out.println("Register request sent");
            TeacherRegisterResponse response=(TeacherRegisterResponse) Main.receiveResponse();
            assert response != null;
            if(response.getMessage().length()==0) System.out.println("Please Try Again");
            else {
                Stage stage=(Stage)registerButton.getScene().getWindow();
                Scene scene=null;
                try {
                    scene=new Scene(loginLoader.load(),registerButton.getScene().getWidth(),registerButton.getScene().getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setTitle("Login");
                stage.setScene(scene);
            }
        }
        else matchLabel.setText("Enter correct info");
    }


    public void switchToLogin(ActionEvent actionEvent) {
        FXMLLoader loginLoader=new FXMLLoader(getClass().getResource("../views/TeacherLoginView.fxml"));
        Stage stage=(Stage)loginLink.getScene().getWindow();
        Scene scene=null;
        try {
            scene=new Scene(loginLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Login");
    }
}
