package main;

import controllers.ResultsController;
import controllers.TeacherHomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import request.Request;
import response.Course;
import response.Response;
import response.TeacherCoursesResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TeacherApplication extends Application {

    private static String teacherId = "";
    static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    public static Object tempHolder = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TeacherApplication.class.getResource("../views/TeacherHomeView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 593);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Teacher Login");
            primaryStage.setMinHeight(590);
            primaryStage.setMinWidth(600);
            primaryStage.show();
            connectToServer();
            TeacherHomeController controller = fxmlLoader.getController();
            controller.callFirst();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 1234);
            outputStream = (ObjectOutputStream) socket.getOutputStream();
            inputStream = (ObjectInputStream) socket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTeacherId() {
        return teacherId;
    }

    public static String setTeacherId(String id) {
        if(teacherId.equals("")) teacherId = id;
        return teacherId;
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
            return (Response) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
