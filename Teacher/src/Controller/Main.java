package Controller;

import Request.Request;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main extends Application {
    Socket socket;
    static ObjectOutputStream oos;
     static ObjectInputStream ois;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../FXML/TeacherLogin.fxml"));
        System.out.println("Creating connection via socket");
        socket=new Socket("localhost",6969);
        System.out.println("Connection created now creating io streams");
        oos=new ObjectOutputStream(socket.getOutputStream());
        ois=new ObjectInputStream(socket.getInputStream());
        System.out.println("io streams created");
        Scene scene=new Scene(loader.load());
        primaryStage.setTitle("Teacher Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void SendRequest(Request request){
        try {
            oos.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object GetResponse(){
        try {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
