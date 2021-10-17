package entity;

import controller.LoginController;
import request.Request;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main extends Application {
    Socket socket=null;
    static ObjectInputStream ois=null;
    static ObjectOutputStream oos=null;
    public static String userRegistrationNumber;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage)  {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
        try {
            System.out.println("Creating a new connection");
            socket=new Socket("localhost",6969);
            ois=new ObjectInputStream(socket.getInputStream());
            oos=new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Connection established and io streams created");
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Sign In");
        try {
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            LoginController login=fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.show();
    }
    public static void sendRequest(Request request){
        try {
            oos.writeObject(request);
            oos.flush();
            System.out.println("Request.Request sent to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object getResponse(){
        try {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
