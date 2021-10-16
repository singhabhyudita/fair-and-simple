package controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.GuiUtil;
import main.TeacherApplication;
import request.SetExamRequest;
import response.Question;
import response.SetExamResponse;
import response.Status;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseController {
    @FXML
    public AnchorPane examScheduleAnchorPane;
    @FXML
    public ComboBox examSearchComboBox;
    @FXML
    public ComboBox sortByComboBox;
    @FXML
    public TableView examsTableView;
    @FXML
    public TableColumn titleTableColumn;
    @FXML
    public TableColumn timeTableColumn;
    @FXML
    public Label examTitleLabel;
    @FXML
    public Text instructionsText;
    @FXML
    public Label startTimeLabel;
    @FXML
    public Label durationLabel;
    @FXML
    public Label maxMarksLabel;
    @FXML
    public TextField titleTextField;
    @FXML
    public DatePicker examDatePicker;
    @FXML
    public TextField endTimeHourTextField;
    @FXML
    public TextField endTimeMinTextField;
    @FXML
    public TextField startTimeHourTextField;
    @FXML
    public TextField startTimeMinTextField;
    @FXML
    public TableView<Question> questionsTableView;
    @FXML
    public TableColumn<Question, String> questionTableColumn;
    @FXML
    public TableColumn<Question, String> optionATableColumn;
    @FXML
    public TableColumn<Question, String> optionBTableColumn;
    @FXML
    public TableColumn<Question, String> optionCTableColumn;
    @FXML
    public TableColumn<Question, String> optionDTableColumn;
    @FXML
    public TableColumn<Question, Integer> correctOptionTableColumn;
    @FXML
    public Button newButton;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button okExamButton;

    private String courseId;

    @FXML
    public void handleOnKeyPressed(KeyEvent keyEvent) {
    }

    @FXML
    public void sortResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void clickItem(MouseEvent mouseEvent) {
    }

    @FXML
    public void searchByPrefix(InputMethodEvent inputMethodEvent) {
    }

    @FXML
    public void newResponse(ActionEvent actionEvent) {
        System.out.println("New question button clicked");
        Stage addQuestionStage = new Stage();
        addQuestionStage.initOwner(deleteButton.getScene().getWindow());
        addQuestionStage.initModality(Modality.WINDOW_MODAL);
        addQuestionStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/NewQuestionView.fxml"));
        try {
            addQuestionStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addQuestionStage.showAndWait();
        if(TeacherApplication.tempHolder == null) return;
        questionsTableView.getItems().add((Question) TeacherApplication.tempHolder);
        TeacherApplication.tempHolder = null;
    }

    @FXML
    public void editResponse(ActionEvent actionEvent) {
        int i = questionsTableView.getSelectionModel().getSelectedIndex();
        Question question = questionsTableView.getSelectionModel().getSelectedItem();
        Stage editQuestionStage = new Stage();
        editQuestionStage.initOwner(deleteButton.getScene().getWindow());
        editQuestionStage.initModality(Modality.WINDOW_MODAL);
        editQuestionStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/NewQuestionView.fxml"));
        try {
            editQuestionStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewQuestionController controller = loader.getController();
        System.out.println(question);
        controller.callFirst(question.getQuestion(), question.getOptionA(), question.getOptionB(),
                question.getOptionC(), question.getOptionD(), question.getCorrectOption());
        editQuestionStage.showAndWait();
        if(TeacherApplication.tempHolder == null) return;
        questionsTableView.getItems().set(i, (Question) TeacherApplication.tempHolder);
        TeacherApplication.tempHolder = null;
    }

    @FXML
    public void deleteResponse(ActionEvent actionEvent) {
        ObservableList<Question> questions = questionsTableView.getItems();
        int i = questionsTableView.getSelectionModel().getSelectedIndex();
        if(i == -1) return;
        questions.remove(i);
    }

    public void callFirst(String courseId) {
        questionTableColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        optionATableColumn.setCellValueFactory(new PropertyValueFactory<>("optionA"));
        optionBTableColumn.setCellValueFactory(new PropertyValueFactory<>("optionB"));
        optionCTableColumn.setCellValueFactory(new PropertyValueFactory<>("optionC"));
        optionDTableColumn.setCellValueFactory(new PropertyValueFactory<>("optionD"));
        correctOptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("correctOption"));
        this.courseId = courseId;
    }

    @FXML
    public void okExamResponse(ActionEvent actionEvent) {
        if(titleTextField.getText().length() == 0) {
            GuiUtil.alert(Alert.AlertType.WARNING, "Specify exam title!");
            return;
        }
        if(startTimeHourTextField.getText().length() == 0 || startTimeMinTextField.getText().length() == 0) {
            GuiUtil.alert(Alert.AlertType.WARNING, "Specify exam start time!");
            return;
        }
        if(endTimeHourTextField.getText().length() == 0 || endTimeMinTextField.getText().length() == 0) {
            GuiUtil.alert(Alert.AlertType.WARNING, "Specify exam end time!");
            return;
        }
        if(examDatePicker.getValue() == null) {
            GuiUtil.alert(Alert.AlertType.WARNING, "Specify exam date!");
            return;
        }
        if(examDatePicker.getValue().isBefore(LocalDate.now())) {
            GuiUtil.alert(Alert.AlertType.WARNING, "Select a future date!");
            return;
        }
        LocalDate date = examDatePicker.getValue();
        Date startTime = new Date(date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                Integer.parseInt(startTimeHourTextField.getText()),
                Integer.parseInt(startTimeMinTextField.getText()));
        Date endTime = new Date(date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                Integer.parseInt(endTimeHourTextField.getText()),
                Integer.parseInt(endTimeMinTextField.getText()));
        String examTitle = examTitleLabel.getText();
        List<Question> questions = new ArrayList<>(questionsTableView.getItems());
        SetExamRequest newExam = new SetExamRequest(TeacherApplication.getTeacherId(),
                this.courseId, startTime, endTime, examTitle, questions);
        Platform.runLater(() -> {

            titleTextField.setEditable(false);
            examDatePicker.setEditable(false);
            startTimeHourTextField.setEditable(false);
            startTimeMinTextField.setEditable(false);
            endTimeHourTextField.setEditable(false);
            endTimeMinTextField.setEditable(false);
            okExamButton.setDisable(true);
            newButton.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setDisable(true);

            TeacherApplication.sendRequest(newExam);
            SetExamResponse response = (SetExamResponse) TeacherApplication.receiveResponse();
            if(response == null || response.getStatus() == Status.OTHER) {
                GuiUtil.alert(Alert.AlertType.WARNING, "Could not schedule exam due to unexpected error!");
                GuiUtil.goToHome((Stage) deleteButton.getScene().getWindow());
            }
            if(response.getStatus() == Status.EXAM_CREATED) {
                GuiUtil.alert(Alert.AlertType.INFORMATION, "Exam scheduled successfully.");
                GuiUtil.goToHome((Stage) deleteButton.getScene().getWindow());
            } else if(response.getStatus() == Status.CLASH) {
                GuiUtil.alert(Alert.AlertType.INFORMATION, "Your exam clashes with other exams.");
                titleTextField.setEditable(true);
                examDatePicker.setEditable(true);
                startTimeHourTextField.setEditable(true);
                startTimeMinTextField.setEditable(true);
                endTimeHourTextField.setEditable(true);
                endTimeMinTextField.setEditable(true);
                okExamButton.setDisable(false);
                newButton.setDisable(false);
                editButton.setDisable(false);
                deleteButton.setDisable(false);
            }
        });
    }
}
