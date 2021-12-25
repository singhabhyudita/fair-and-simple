package controllers;

import com.jfoenix.controls.JFXButton;
import entity.Exam;
import entity.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;
import request.GetResultForStudentRequest;
import response.GetResultForStudentResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StudentResultCardController {

    @FXML
    public Label nameLabel;
    @FXML
    public JFXButton resultButton;

    private Student student;
    private Exam exam;

    public void first(Student student, Exam exam) {
        nameLabel.setText(student.getName());
        this.student = student;
        this.exam = exam;
    }

    public void viewResult(ActionEvent actionEvent) {
        GetResultForStudentRequest request = new GetResultForStudentRequest(String.valueOf(this.student.getRegistrationNumber()), this.exam.getExamId(), this.exam.getExamId());
        Main.sendRequest(request);
        GetResultForStudentResponse response = (GetResultForStudentResponse) Main.receiveResponse();
        if(response == null) return;
        if(!response.isCorrected()) {
            System.out.println("Will open correction screen");
            openCorrectionScreen(response);
        } else {
            System.out.println("Will open result screen");
            openResultScreen(response);
        }
    }

    private void openResultScreen(GetResultForStudentResponse response) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/QuizResultView.fxml"));
        Stage stage = new Stage();
        Scene scene=null;
        try {
            scene=new Scene(fxmlLoader.load(),nameLabel.getScene().getWidth(),nameLabel.getScene().getHeight());
            QuizResultController controller = fxmlLoader.getController();
            controller.setValues(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Result for " + nameLabel.getText());
        stage.show();
    }

    private void openCorrectionScreen(GetResultForStudentResponse response) {
        System.out.println("StudentResultCardController.openCorrectionScreen");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/QuizCorrectionView.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),nameLabel.getScene().getWidth(),nameLabel.getScene().getHeight());
            QuizCorrectionController controller = fxmlLoader.getController();
            System.out.println("Setting values inside controller");
            controller.setValues(this.exam.getExamId(), String.valueOf(this.student.getRegistrationNumber()), response.getQuestions(), response.getSubjectiveAnswer());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Responses of " + nameLabel.getText());
        stage.show();
    }
}
