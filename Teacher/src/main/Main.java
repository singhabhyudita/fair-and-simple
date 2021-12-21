package main;

import entity.Status;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import request.Request;
import response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main extends Application {

    private static String teacherId = "";
    private static String teacherName = "";
    static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    public static Object tempHolder = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../views/TeacherLoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 590, 600);
//            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Teacher Login");
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(590);
//            primaryStage.setAlwaysOnTop(true);
//            primaryStage.setMaximized(true);
//            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.show();
            connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 6969);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTeacherId() {
        return teacherId;
    }

    public static String getTeacherName() {
        return teacherName;
    }

    public static void setTeacherId(String id) {
        if(teacherId.equals("") || id.equals("")) teacherId = id;
    }

    public static void setTeacherName(String teacherName) {
        Main.teacherName = teacherName;
    }

    public static void sendRequest(Request request) {
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Response receiveResponse() {
        try {
            Object obj=inputStream.readObject();
            System.out.println("Response is " + obj);
            return (Response) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
