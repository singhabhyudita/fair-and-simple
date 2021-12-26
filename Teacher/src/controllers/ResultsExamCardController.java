package controllers;

import com.jfoenix.controls.JFXButton;
import entity.Exam;
import entity.Student;
import entity.StudentResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.Main;
import request.CourseStudentRequest;
import response.CourseStudentResponse;

import java.io.IOException;

public class ResultsExamCardController {

    @FXML
    public JFXButton viewResultButton;
    @FXML
    public Label titleLabel;
    @FXML
    public Label courseLabel;
    @FXML
    public Label marksLabel;

    private Exam exam;
    private VBox studentsVBoxReference;

    public void first(Exam exam, VBox studentsVBox) {
        this.exam = exam;
        titleLabel.setText(exam.getTitle());
        courseLabel.setText(exam.getCourseName());
        marksLabel.setText("Max marks: " + exam.getMaxMarks());
        studentsVBoxReference = studentsVBox;
    }

    public void viewResult(ActionEvent actionEvent) {
        studentsVBoxReference.getChildren().clear();
        CourseStudentRequest request = new CourseStudentRequest(exam.getCourseId());
        Main.sendRequest(request);
        CourseStudentResponse response = (CourseStudentResponse) Main.receiveResponse();
        if(response == null) return;
        for(Student student : response.getStudents()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/StudentResultCardView.fxml"));
            try {
                Node node = fxmlLoader.load();
                StudentResultCardController controller = fxmlLoader.getController();
                controller.first(student, exam);
                studentsVBoxReference.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
