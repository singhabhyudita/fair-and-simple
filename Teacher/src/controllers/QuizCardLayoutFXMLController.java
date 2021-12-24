package controllers;

import com.jfoenix.controls.JFXButton;
import entity.Exam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizCardLayoutFXMLController implements Initializable {
    @FXML
    public Label title;
    @FXML
    public Label noq;
    @FXML
    public Label course;
    @FXML
    public Label startTimeLabel;
    @FXML
    public Label endTimeLabel;
    @FXML
    public JFXButton startButton;

    private Exam exam;

    public void setExam(Exam exam) {
        this.exam = exam;
        this.title.setText(this.exam.getTitle());
    }

    public void setStartTimeLabel(String startTime) {
        this.startTimeLabel.setText(startTime);
    }

    public void setEndTimeLabel(String endTime) {
        this.endTimeLabel.setText(endTime);
    }

    public void setCourse(String course) {
        this.course.setText(course);
    }

    public void setNoq(String value) {
        this.noq.setText("Number of questions: " + value);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void checkProctorJoined(ActionEvent actionEvent) {

    }
}
