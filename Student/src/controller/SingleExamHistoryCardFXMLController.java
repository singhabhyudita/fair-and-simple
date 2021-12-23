package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SingleExamHistoryCardFXMLController {
    @FXML
    public Label titleLabel;
    @FXML
    public Label courseLabel;
    @FXML
    public Label marksLabel;

    public void setTitleLabel(String title) {
        this.titleLabel.setText(title);
    }

    public void setCourseLabel(String course) {
        this.courseLabel.setText(course);
    }

    public void setMarksLabel(String marks) {
        this.marksLabel.setText(marks);
    }

    @FXML
    public Button viewExamButton;


    public void viewResult(ActionEvent actionEvent) {
        // TODO:
    }
}
