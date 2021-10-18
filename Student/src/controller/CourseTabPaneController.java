package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import request.*;
import response.*;
import entity.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CourseTabPaneController implements Initializable
{
    @FXML
    public Label courseNameLabel;
    @FXML
    public Label courseCodeLabel;
    @FXML
    public Text aboutCourseLabel;
    @FXML
    public Label professorLabel;
    @FXML
    private Text instructionsText;
    @FXML
    private Label durationLabel;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label maxMarksLabel;
    @FXML
    private Label examTitleLabel;
    @FXML
    private Label Label;
    @FXML
    private TableView examsTableView;
    @FXML
    private TableView participantsTableView;
    @FXML
    private TextArea examsTextArea;
    @FXML
    private Button sendButton;
    @FXML
    private TableColumn titleTableColumn;
    @FXML
    private TableColumn timeTableColumn;
    @FXML
    private ComboBox examSearchComboBox;
    @FXML
    private TableColumn nameTableColumn;
    @FXML
    private TableColumn registrationNumberTableColumn;

    public ObservableList<Exam> observableExamsList;
    public ObservableList<Student> observableParticipantsList;

    private SortedList<Exam> sortedData;
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void first(String courseId) throws InterruptedException {
        this.courseId = courseId;
        Main.sendRequest(new CourseDetailsRequest(courseId));
        System.out.println("course details request sent");
        Course course= (Course) Main.getResponse();
        assert course != null;
        courseNameLabel.setText(course.getCourseName());
        courseCodeLabel.setText(course.getCourseCode());
        aboutCourseLabel.setText(course.getCourseDescription());
        professorLabel.setText(course.getTeacherId());
        // Populating the Title and Time column in TableView with Classes.Exam object properties
        titleTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("title")
        );
        timeTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("startTime")
        );
        updateExamsScheduleTable();

        nameTableColumn.setCellValueFactory(
                new PropertyValueFactory<Student, String>("name")
        );
        registrationNumberTableColumn.setCellValueFactory(
                new PropertyValueFactory<Student, Integer>("registrationNumber")
        );
        updateParticipantsTable();

    }

    @FXML
    public void refreshExamsScheduleButtonResponse(ActionEvent e) {
        updateExamsScheduleTable();
    }
    @FXML
    public void refreshCourseInfoButtonResponse(ActionEvent e) {
        updateParticipantsTable();
    }

    public void updateParticipantsTable() {
        //Sending request to server to fetch list of participants of this course
        ParticipantsListRequest participantsListRequest = new ParticipantsListRequest(courseId);
        Main.sendRequest(participantsListRequest);
        ParticipantsListResponse participantsListResponse = (ParticipantsListResponse) Main.getResponse();

        ////Setting list of exams as Observable list in Exams Table
        observableParticipantsList = FXCollections.observableList(participantsListResponse.getParticipantsList());
        participantsTableView.setItems(observableParticipantsList);
    }
    public void updateExamsScheduleTable(){
        //Sending request to server to fetch list of exams
        ExamsListRequest examsListRequest = new ExamsListRequest(courseId);
        Main.sendRequest(examsListRequest);
        ExamsListResponse examsListResponse = (ExamsListResponse) Main.getResponse();

        //Setting list of exams as Observable list in Exams Table
        observableExamsList = FXCollections.observableList(examsListResponse.getExamsList());
        examsTableView.setItems(observableExamsList);

        //Select the first value in exams
        if (examsListResponse.getExamsList().size() != 0)
            examsTableView.getSelectionModel().select(0);

        //Wrap the Exams List in a SortedList to sort in ascending or descending order wrt start time
        sortedData = new SortedList<>(observableExamsList);

        //Bind the SortedList comparator to the TableView comparator.
        //Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(examsTableView.comparatorProperty());

        //Add sorted data to the table.
        examsTableView.setItems(sortedData);
    }


    @FXML
    public void backResponse(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Parent root = (Parent) loader.load();

    }

    public void refreshResponse(ActionEvent actionEvent) {

    }

    public void sendButtonResponse(ActionEvent actionEvent) {
        //TODO: send messages
    }
}
