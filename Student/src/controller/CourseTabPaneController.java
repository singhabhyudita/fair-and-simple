package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import request.*;
import response.*;
import entity.*;
import util.ChatUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CourseTabPaneController implements Initializable {
    @FXML
    public TabPane courseTabPane;
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
    public Tab discussionForumTab;
    @FXML
    public TextField sendTextField;
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
    private VBox chatContainer;
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
    @FXML
    private ScrollPane chatScrollPane;

    public ObservableList<Exam> observableExamsList;
    public ObservableList<Student> observableParticipantsList;

    private SortedList<Exam> sortedData;
    private String courseId, name;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatScrollPane.vvalueProperty().bind(chatContainer.heightProperty());
    }

    public void first(String courseId, String name) throws InterruptedException {
        Main.chatVBox = chatContainer;
        Main.lastOpenCourseId = courseId;
        this.courseId = courseId;
        this.name = name;
        Main.sendRequest(new CourseDetailsRequest(courseId));
        System.out.println("course details request sent " + courseId);
        Course course = (Course) Main.getResponse();
        System.out.println("Course object selected is " + course);
        assert course != null;

        System.out.println("Course Object = " + course);

        courseNameLabel.setText(course.getCourseName());
        courseCodeLabel.setText(course.getCourseCode());
        aboutCourseLabel.setText(course.getCourseDescription());
        professorLabel.setText(course.getTeacherName());
//         Populating the Title and Time column in TableView with Classes.Exam object properties
        titleTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("title")
        );
        timeTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("date")
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
        System.out.println("participants list requested for course id " + courseId);
        Main.sendRequest(participantsListRequest);

        ParticipantsListResponse participantsListResponse = (ParticipantsListResponse) Main.getResponse();
        System.out.println("participants list response received " + participantsListResponse);
        assert participantsListResponse != null;
        observableParticipantsList = FXCollections.observableList(participantsListResponse.getParticipantsList());
        participantsTableView.setItems(observableParticipantsList);
    }

    ExamsListResponse examsListResponse;

    public void updateExamsScheduleTable() {
        //Sending request to server to fetch list of exams
        ExamsListRequest examsListRequest = new ExamsListRequest(courseId);
        Main.sendRequest(examsListRequest);
        examsListResponse = (ExamsListResponse) Main.getResponse();

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

    public void sendButtonResponse() {
        //TODO: send messages
        String text = sendTextField.getText().trim();
        if(text == "")
            return;
        sendTextField.clear();
        Main.sendRequest(new Message(Main.userRegistrationNumber, name, courseId, courseNameLabel.getText(),text, null,
                new Timestamp(System.currentTimeMillis()), true));
        System.out.println("Main.userRegistrationNumber:" + Main.userRegistrationNumber);
        SendMessageResponse sendMessageResponse = (SendMessageResponse) Main.getResponse();
        assert sendMessageResponse != null;

        System.out.println(sendMessageResponse.getResponse());
    }

    public void refreshDiscussionForumButtonResponse(ActionEvent actionEvent) {
//        ArrayList <Message> messages = new ArrayList<>();
//        messages.add(new Message("1","Saurabh","1","I am Saurabh.",null,new Timestamp((new Date()).getTime()),true));
//        for(Message message : messages) {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SingleChatCardFXML.fxml"));
//            try {
//                Node node = fxmlLoader.load();
//                SingleChatCardFXMLController singleChatCardFXMLController = fxmlLoader.getController();
//                singleChatCardFXMLController.messageLabel.setText(message.getText());
//                singleChatCardFXMLController.nameLabel.setText(message.getSenderName());
//                singleChatCardFXMLController.timestampLabel.setText(message.getSentAt().toString());
//                singleChatCardFXMLController.nameHBox.backgroundProperty().set(new Background(new BackgroundFill(Color.web("#bee2f7"),
//                        CornerRadii.EMPTY,
//                        Insets.EMPTY)));
//                chatContainer.getChildren().add(node);
//                Notifications.create()
//                        .title("Message received!")
//                        .text("Message has been received!")
//                        .showInformation();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void backFromDiscussionForumButtonResponse(ActionEvent actionEvent) {
        Main.chatVBox = null;
        Main.lastOpenCourseId = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) backFromDiscussionForumButton.getScene().getWindow();
        ProfileScreenController profileScreenController = loader.getController();
        profileScreenController.first(name);
        stage.setTitle("Profile");
        stage.setScene(scene);
    }

    public void backFromExamsScheduleButtonResponse(ActionEvent actionEvent) {
        Main.chatVBox = null;
        Main.lastOpenCourseId = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) backFromExamsScheduleButton.getScene().getWindow();
        ProfileScreenController profileScreenController = loader.getController();
        profileScreenController.first(name);
        stage.setTitle("Profile");
        stage.setScene(scene);
    }

    public void backfromCourseInfoButtonResponse(ActionEvent actionEvent) {
        Main.chatVBox = null;
        Main.lastOpenCourseId = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) backfromCourseInfoButton.getScene().getWindow();
        ProfileScreenController profileScreenController = loader.getController();
        profileScreenController.first(name);
        stage.setTitle("Profile");
        stage.setScene(scene);
    }

    public void clickItem(MouseEvent mouseEvent) {
    }

    public void sortResponse(ActionEvent actionEvent) {
    }

    public void handleOnKeyPressed(KeyEvent keyEvent) {
    }

    public void onExamClicked(MouseEvent mouseEvent) {
        Exam selectedExam = (Exam) examsTableView.getSelectionModel().getSelectedItem();
        examTitleLabel.setText(selectedExam.getTitle());
        instructionsText.setText(selectedExam.getDescription());
        Timestamp startTime = new Timestamp(selectedExam.getDate().getTime());
        Timestamp endTime = new Timestamp(selectedExam.getEndTime().getTime());
        startTimeLabel.setText(startTime.toString());
        Duration duration = Duration.between(startTime.toLocalDateTime(), endTime.toLocalDateTime());
        long minutes = duration.toMinutes();
        durationLabel.setText(String.valueOf(minutes));
        maxMarksLabel.setText(selectedExam.getMaxMarks().toString());
    }

    public void onChatClicked(Event event) {
        chatContainer.getChildren().clear();
        Main.sendRequest(new DisplayMessagesRequest(courseId));
        DisplayMessagesResponse displayMessagesResponse = (DisplayMessagesResponse) Main.getResponse();
        ArrayList<Message> messages = displayMessagesResponse.getMessages();

        for (Message message : messages) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SingleChatCardFXML.fxml"));
            try {
                Node node = fxmlLoader.load();
                SingleChatCardFXMLController singleChatCardFXMLController = fxmlLoader.getController();
                singleChatCardFXMLController.messageLabel.setText(message.getText());
                singleChatCardFXMLController.messageLabel.setAlignment(message.getSenderID().equals(Main.userRegistrationNumber) ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
                singleChatCardFXMLController.nameLabel.setText(message.getSenderName());
                singleChatCardFXMLController.timestampLabel.setText(message.getSentAt().toString());
                singleChatCardFXMLController.nameHBox.backgroundProperty().set(new Background(new BackgroundFill(Color.web(
                        (message.getSenderID().equals(Main.userRegistrationNumber)) ? Main.myColor : Main.otherColor),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
                chatContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
