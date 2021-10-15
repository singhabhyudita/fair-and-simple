package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiUtil {
    public static void goToHome(Stage stage) {
        FXMLLoader loader = new FXMLLoader(GuiUtil.class.getResource("../views/TeacherHomeView.fxml"));
        Scene homeScene = null;
        try {
            homeScene = new Scene(loader.load());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Critical Error. Closing the application.");
            System.exit(0);
        }
        stage.setScene(homeScene);
    }

    public static void alert(Alert.AlertType type, String text) {
        Alert alert = new Alert(type, text);
        alert.showAndWait();
    }

}
