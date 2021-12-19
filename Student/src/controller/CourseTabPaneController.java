package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    public Button backFromDiscussionForumButton;
    @FXML
    public Button refreshDiscussionForumButton;
    @FXML
    public Button backFromExamsScheduleButton;
    @FXML
    public Button refreshExamsScheduleButton;
    @FXML
    public Button backfromCourseInfoButton;
    @FXML
    public Button refreshCourseInfoButton;
    @FXML
    public Button leaveCourseButton;
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
    private String courseId,name;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void first(String courseId,String name) throws InterruptedException {
        this.courseId = courseId;
        this.name=name;
        Main.sendRequest(new CourseDetailsRequest(courseId));
        System.out.println("course details request sent "+ courseId);
        Course course= (Course) Main.getResponse();
        System.out.println("Course object selected is "+course);
        assert course != null;

        System.out.println("Course Object = " + course);

        courseNameLabel.setText(course.getCourseName());
        courseCodeLabel.setText(course.getCourseCode());
        aboutCourseLabel.setText(course.getCourseDescription());
        professorLabel.setText(course.getTeacherName());
        // Populating the Title and Time column in TableView with Classes.Exam object properties
//        titleTableColumn.setCellValueFactory(
//                new PropertyValueFactory<Exam, String>("title")
//        );
//        timeTableColumn.setCellValueFactory(
//                new PropertyValueFactory<Exam, String>("startTime")
//        );
//        updateExamsScheduleTable();

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
        System.out.println("participants list requested for course id "+courseId);
        Main.sendRequest(participantsListRequest);

        ParticipantsListResponse participantsListResponse = (ParticipantsListResponse) Main.getResponse();
        System.out.println("participants list response received "+participantsListResponse);
        assert participantsListResponse != null;
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

    public void sendButtonResponse(ActionEvent actionEvent) {
        //TODO: send messages
    }
<<<<<<< HEAD

    public void refreshDiscussionForumButtonResponse(ActionEvent actionEvent) {
    }

    public void backFromDiscussionForumButtonResponse(ActionEvent actionEvent) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene=new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage= (Stage) backFromDiscussionForumButton.getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        ProfileScreenController profileScreenController=loader.getController();
        profileScreenController.first(name);
    }

    public void onExamClicked(MouseEvent mouseEvent) {
=======

    public void refreshDiscussionForumButtonResponse(ActionEvent actionEvent) {
    }
>>>>>>> 1b53e4842c54504540b432792a0702d1a73cf878

    public void backFromDiscussionForumButtonResponse(ActionEvent actionEvent) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene=new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage= (Stage) backFromDiscussionForumButton.getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        ProfileScreenController profileScreenController=loader.getController();
        profileScreenController.first(name);
    }

<<<<<<< HEAD
    public void backFromExamsScheduleButtonResponse(ActionEvent actionEvent) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene=new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage= (Stage) backFromExamsScheduleButton.getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        ProfileScreenController profileScreenController=loader.getController();
        profileScreenController.first(name);
    }
=======
    public void onExamClicked(MouseEvent mouseEvent) {
>>>>>>> 1b53e4842c54504540b432792a0702d1a73cf878

    public void backfromCourseInfoButtonResponse(ActionEvent actionEvent) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene=new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage= (Stage) backfromCourseInfoButton.getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        ProfileScreenController profileScreenController=loader.getController();
        profileScreenController.first(name);
    }
    public void handleOnKeyPressed(KeyEvent keyEvent) {
    }

<<<<<<< HEAD
    public void clickItem(MouseEvent mouseEvent) {
    }

    public void sortResponse(ActionEvent actionEvent) {
    }

=======
    public void backFromExamsScheduleButtonResponse(ActionEvent actionEvent) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene=new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage= (Stage) backFromExamsScheduleButton.getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        ProfileScreenController profileScreenController=loader.getController();
        profileScreenController.first(name);
    }

    public void backfromCourseInfoButtonResponse(ActionEvent actionEvent) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene=new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage= (Stage) backfromCourseInfoButton.getScene().getWindow();
        stage.setTitle("Profile");
        stage.setScene(scene);
        ProfileScreenController profileScreenController=loader.getController();
        profileScreenController.first(name);
    }
>>>>>>> 1b53e4842c54504540b432792a0702d1a73cf878
    public void handleOnKeyPressed(KeyEvent keyEvent) {
    }

    public void clickItem(MouseEvent mouseEvent) {
    }

    public void sortResponse(ActionEvent actionEvent) {
    }
}
