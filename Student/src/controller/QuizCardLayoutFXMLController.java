package controller;

import com.jfoenix.controls.JFXButton;
import entity.Exam;
import entity.GuiUtil;
import entity.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import request.GetQuestionsRequest;
import response.GetQuestionsResponse;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class QuizCardLayoutFXMLController implements Initializable {
    @FXML
    public Label title;
    @FXML
    public Label noq;
    @FXML
    public Label course;
    @FXML
    public JFXButton startButton;

    private Exam exam;

    public void setExam(Exam exam) {
        this.exam = exam;
        this.title.setText(this.exam.getTitle());
    }

    public void setCourse(String course) {
        this.course.setText(course);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setNoq(String value) {
        this.noq.setText("Number of questions: " + value);
    }

    public void startQuiz(ActionEvent actionEvent) {
        if(exam.getDate().getTime() - (new Date()).getTime() > 0) {
            GuiUtil.alert(Alert.AlertType.WARNING,"Exam hasn't started yet!");
        }
        else {
            GetQuestionsResponse response = getData(exam);
            if(response.getProctorPort() == -1) {
                GuiUtil.alert(Alert.AlertType.ERROR, "Exam will start only after the proctor joins!");
                return;
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/QuestionsScreenFXML.fxml"));
            Stage currentStage=(Stage)title.getScene().getWindow();
            Scene scene;
            try {
                scene=new Scene(fxmlLoader.load());
                QuestionsScreenController questionsScreenController= fxmlLoader.getController();
                questionsScreenController.setQuiz(exam);
                questionsScreenController.setData(response.getProctorPort(), response.getQuestionsList(), exam.getExamId());
                currentStage.setScene(scene);
                currentStage.setTitle(exam.getTitle());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private GetQuestionsResponse getData(Exam exam) {
        if (exam != null) {
            GetQuestionsRequest getQuestionsRequest = new GetQuestionsRequest(exam.getExamId());
            Main.sendRequest(getQuestionsRequest);
            return (GetQuestionsResponse) Main.getResponse();
        }
        return null;
    }
}
