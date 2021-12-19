package controllers;

import entity.Exam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.GuiUtil;

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
    }
}