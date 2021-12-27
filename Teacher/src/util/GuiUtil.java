package util;

import controller.TeacherHomeController;
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
            homeScene = new Scene(loader.load(),stage.getScene().getWidth(),stage.getScene().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Critical Error. Closing the application.");
            alert.showAndWait();
            System.exit(0);
        }
        stage.setScene(homeScene);
        TeacherHomeController teacherHomeController=loader.getController();
        teacherHomeController.callFirst();
    }

    public static void alert(Alert.AlertType type, String text) {
        Alert alert = new Alert(type, text);
        alert.showAndWait();
    }

}
