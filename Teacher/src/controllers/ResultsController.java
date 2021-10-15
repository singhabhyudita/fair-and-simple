package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.GuiUtil;
import main.TeacherApplication;
import request.ExamsRequest;
import response.Exam;
import response.ExamsResponse;

import java.io.IOException;
import java.util.Date;

public class ResultsController {
    @FXML
    public Button backButton;
    @FXML
    public Button refreshButton;
    @FXML
    public ComboBox resultSearchComboBox;
    @FXML
    public ComboBox sortByComboBox;
    @FXML
    public TableView<Exam> examsTableView;
    @FXML
    public TableColumn<Exam, String> titleTableColumn;
    @FXML
    public TableColumn<Exam, Date> dateTableColumn;
    @FXML
    public Label examTitleLabel;
    @FXML
    public TableColumn rankTableColumn;
    @FXML
    public TableColumn registrationNumberTableColumn;
    @FXML
    public TableColumn nameTableColumn;
    @FXML
    public TableColumn marksTableColumn;

    @FXML
    public void backResponse(ActionEvent actionEvent) {
        GuiUtil.goToHome((Stage) backButton.getScene().getWindow());
    }

    @FXML
    public void refreshResponse(ActionEvent actionEvent) {
        callFirst();
    }

    @FXML
    public void handleOnKeyPressed(KeyEvent keyEvent) {
    }

    @FXML
    public void sortResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void clickItem(MouseEvent mouseEvent) {
    }

    public void callFirst() {
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        Platform.runLater(() -> {
            ExamsRequest examsRequest = new ExamsRequest(TeacherApplication.getTeacherId(), true);
            TeacherApplication.sendRequest(examsRequest);
            ExamsResponse examsResponse = (ExamsResponse) TeacherApplication.receiveResponse();
            System.out.println("Exams response = " + examsResponse);
            if(examsResponse == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Couldn't fetch the exams list. Might be a server error.");
                alert.show();
                GuiUtil.goToHome((Stage) backButton.getScene().getWindow());
            } else {
                ObservableList<Exam> examList = FXCollections.observableList(examsResponse.getExams());
                examsTableView.setItems(examList);
            }
        });
    }
}