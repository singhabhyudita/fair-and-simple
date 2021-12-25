package controller;

import entity.Exam;
import entity.GuiUtil;
import entity.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import request.GetResultForStudentRequest;
import response.GetResultForStudentResponse;

import java.io.IOException;

public class SingleExamHistoryCardFXMLController {
    @FXML
    public Label titleLabel;
    @FXML
    public Label courseLabel;
    @FXML
    public Label marksLabel;

    private Exam exam;

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
        GetResultForStudentRequest request = new GetResultForStudentRequest(Main.userRegistrationNumber, this.exam.getExamId(), this.exam.getExamId());
        Main.sendRequest(request);
        GetResultForStudentResponse response = (GetResultForStudentResponse) Main.getResponse();
        if(response == null) return;
        if(!response.isCorrected()) {
            System.out.println("Will open correction screen");
            GuiUtil.alert(Alert.AlertType.INFORMATION, "Your responses haven't been corrected yet.");
        } else {
            System.out.println("Will open result screen");
            openResultScreen(response);
        }
    }

    private void openResultScreen(GetResultForStudentResponse response) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/QuizResultView.fxml"));
        Stage stage = new Stage();
        Scene scene=null;
        try {
            scene=new Scene(fxmlLoader.load());
            QuizResultController controller = fxmlLoader.getController();
            controller.setValues(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Your Result");
        stage.show();
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
