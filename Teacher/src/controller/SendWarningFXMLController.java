package controller;

import entity.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SendWarningFXMLController {
    @FXML
    public TextArea warningTextArea;
    @FXML
    public Button sendWarningButton;
    @FXML
    public Label warnLabel;

    private String regNo, name;

    public void setWarnLabel(String name, String regNo) {
        this.name = name;
        this.regNo = regNo;
        this.warnLabel.setText("Warn " + name + " ("+regNo+")");
    }


    public void sendWarning(ActionEvent actionEvent) {
        if(warningTextArea.getText().trim().length() == 0)
            return;
        Warning warning = new Warning(Main.getTeacherId(),Main.getTeacherName(), regNo,null,"Exam",
                warningTextArea.getText().trim(),null,Timestamp.valueOf(LocalDateTime.now()),false);
        try {
            Main.outputStream.writeObject(warning);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) warnLabel.getScene().getWindow()).close();
    }
}
