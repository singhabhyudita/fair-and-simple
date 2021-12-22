package controller;


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
        System.out.println("Creating a request object");
        LoginRequest request=new LoginRequest(usernameField.getText(),passwordField.getText());
        Main.sendRequest(request);
        System.out.println("Request.Request Sent");
        LoginResponse response= (LoginResponse) Main.getResponse();
        if (response != null && response.getFirstName() == null) {
            System.out.println("Wrong Info");
        }
        else if(response !=null){
            Main.userRegistrationNumber = String.valueOf(response.getRegistrationNo());
            startMessageThread();
            System.out.println("Registration number is "+response.getRegistrationNo());
            FXMLLoader homepageLoader= new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
            Stage currentStage=(Stage)loginButton.getScene().getWindow();
            Scene scene=null;
            try {
                 scene=new Scene(homepageLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentStage.setScene(scene);
            currentStage.setTitle("Welcome");
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
        stage.setTitle("Sign Up");
    }
}
