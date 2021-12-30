package entity;

import controller.LoginController;
import controller.ProfileScreenController;
import javafx.scene.layout.VBox;
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
    public static Socket socket=null,chatSocket=null;
    public static ObjectInputStream ois=null;
    public static ObjectOutputStream oos=null;
    public static String userRegistrationNumber;
    public static VBox chatVBox = null;
    public static VBox notificationVbox=null;
    public static String lastOpenCourseId = null;
    public static final String myColor = "#f55f78";
    public static final String otherColor = "#bee2f7";
    public static String userFullName = "Pionel Pepsi";
    public static ProfileScreenController profileScreenController = null;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage)  {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
        try {
            System.out.println("Creating a new connection");
            socket=new Socket("192.168.0.112",6969);
            System.out.println(socket);
            oos=new ObjectOutputStream(socket.getOutputStream());
            ois=new ObjectInputStream(socket.getInputStream());
            System.out.println("Connection established and io streams created");

            System.out.println(Thread.currentThread());
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
            System.out.println("response is sent on Main method");
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
