package entity;
import javafx.scene.control.Alert;

public class GuiUtil {
    public static void alert(Alert.AlertType type, String text) {
        Alert alert = new Alert(type, text);
        alert.showAndWait();
    }
}
