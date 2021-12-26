package controller;


import entity.GuiUtil;
import entity.Main;
import request.LoginRequest;
import response.LoginResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.ChatUtil;
import util.HashUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    
    @FXML
    public AnchorPane loginPane;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void login(ActionEvent actionEvent) {
        LoginRequest request=new LoginRequest(usernameField.getText(), HashUtil.getMd5(passwordField.getText()));
        Main.sendRequest(request);
        LoginResponse response= (LoginResponse) Main.getResponse();
        if (response ==null)
            GuiUtil.alert(Alert.AlertType.ERROR,"Incorrect Information.Please try again.");
        else {
            Main.userRegistrationNumber = String.valueOf(response.getRegistrationNo());
            startMessageThread();
            FXMLLoader homepageLoader= new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
            Stage currentStage=(Stage)loginButton.getScene().getWindow();
            Scene scene=null;
            try {
                 scene=new Scene(homepageLoader.load(), loginButton.getScene().getWidth(), loginButton.getScene().getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentStage.setScene(scene);
            currentStage.setMaximized(true);
            currentStage.setTitle("Welcome");
            Main.userFullName = response.getFirstName() + " " + response.getLastName();
            ProfileScreenController profileScreenController=homepageLoader.getController();
            profileScreenController.first(response.getFirstName()+" "+response.getLastName());
        }
    }

    private void startMessageThread() {
        Socket chatSocket;
        ObjectInputStream chatois = null;
        try {
            chatSocket = new Socket("localhost",6970);
            System.out.println(chatSocket);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(chatSocket.getOutputStream());
            System.out.println(objectOutputStream);
            objectOutputStream.writeObject(Main.userRegistrationNumber);
            objectOutputStream.flush();
            InputStream is = chatSocket.getInputStream();
            chatois=new ObjectInputStream(is);
            System.out.println(chatois);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread t=new Thread(new ChatUtil(chatois));
        t.start();
        loginButton.getScene().getWindow().setOnCloseRequest(event -> {
            t.interrupt();
            System.out.println("Thread is interrupted");
        });
    }

    public void switchToSignup(ActionEvent actionEvent) {
        FXMLLoader registerLoader=new FXMLLoader(getClass().getResource("../fxml/Register.fxml"));
        Scene scene=null;
        Stage stage=(Stage)signupLink.getScene().getWindow();
        try {
            scene=new Scene(registerLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        RegisterController registerController=registerLoader.getController();
        registerController.first();
        stage.setTitle("Sign Up");
    }
}
