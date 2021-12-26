package controllers;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import request.AddStudentRequest;
import response.*;
import util.GuiUtil;
import main.Main;
import request.CourseStudentRequest;
import request.DisplayMessagesRequest;
import request.SetExamRequest;
import sun.awt.image.ToolkitImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
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
    public TableView<Exam> examsTableView;
    @FXML
    public TableColumn<Exam, String> titleTableColumn;
    @FXML
    public TableColumn<Exam, Date> timeTableColumn;
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
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public Label endTimeLabel;
    @FXML
    public TableView<Student> courseStudentTableView;
    @FXML
    public TableColumn<Student, String> nameTableColumn;
    @FXML
    public TableColumn<Student, String> registrationNumberTableColumn;
    @FXML
    public TextField proctorIDTextField;
    @FXML
    public VBox chatContainer;
    @FXML
    public TextField sendTextField;
    @FXML
    public Button uploadImageButton;
    @FXML
    public ScrollPane chatScrollPane;
    @FXML
    public TextField addStudentTextField;
    @FXML
    public Button addStudentButton;

    File selectedFile = null;
    private String courseId;
    private List<Student> students;
    private String courseName;

    @FXML
    public void handleOnKeyPressed(KeyEvent keyEvent) {
    }

    @FXML
    public void sortResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void onExamClicked(MouseEvent mouseEvent) {
        Exam selectedExam = examsTableView.getSelectionModel().getSelectedItem();
        examTitleLabel.setText(selectedExam.getTitle());
        instructionsText.setText(selectedExam.getDescription());
        Timestamp startTime = new Timestamp(selectedExam.getDate().getTime());
        Timestamp endTime = new Timestamp(selectedExam.getEndTime().getTime());
        startTimeLabel.setText(startTime.toString());
        endTimeLabel.setText(endTime.toString());
        maxMarksLabel.setText(selectedExam.getMaxMarks().toString());
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
            addQuestionStage.setScene(new Scene(loader.load(),deleteButton.getScene().getWidth(),deleteButton.getScene().getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addQuestionStage.showAndWait();
        if(Main.tempHolder == null) return;
        questionsTableView.getItems().add((Question) Main.tempHolder);
        Main.tempHolder = null;
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
        if(Main.tempHolder == null) return;
        questionsTableView.getItems().set(i, (Question) Main.tempHolder);
        Main.tempHolder = null;
    }

    @FXML
    public void deleteResponse(ActionEvent actionEvent) {
        ObservableList<Question> questions = questionsTableView.getItems();
        int i = questionsTableView.getSelectionModel().getSelectedIndex();
        if(i == -1) return;
        questions.remove(i);
    }

    public void callFirst(String courseId, String courseName, List<Exam> courseExams) {
        Main.chatVBox = chatContainer;
        Main.lastOpenCourseId = courseId;
        chatScrollPane.vvalueProperty().bind(chatContainer.heightProperty());
        this.courseName = courseName;
        this.courseId = courseId;

        descriptionTextArea.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,250}") ? c : null));
        titleTextField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,20}") ? c : null));
        addStudentTextField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,8}") ? c : null));
        questionTableColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        optionATableColumn.setCellValueFactory(new PropertyValueFactory<>("optionA"));
        optionBTableColumn.setCellValueFactory(new PropertyValueFactory<>("optionB"));
        optionCTableColumn.setCellValueFactory(new PropertyValueFactory<>("optionC"));
        optionDTableColumn.setCellValueFactory(new PropertyValueFactory<>("optionD"));
        correctOptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("correctOption"));

        populateExamsTable(courseExams);
        populateCourseStudentsTable();
    }

    private void populateCourseStudentsTable() {
        System.out.println("Now populating the students of this course");
        System.out.println("Course id set = " + courseId);
        CourseStudentRequest request = new CourseStudentRequest(courseId);
        Main.sendRequest(request);
        CourseStudentResponse response = (CourseStudentResponse) Main.receiveResponse();
        students = response.getStudents();
        System.out.println("Students = " + students);
        System.out.println("Number of students = " + students.size());
        for(Student s : students) System.out.println(s.getRegistrationNumber());
        registrationNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ObservableList<Student> studentsObv = FXCollections.observableList(new ArrayList<>(students));
        courseStudentTableView.setItems(studentsObv);
    }

    private void populateExamsTable(List<Exam> courseExams) {
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        ObservableList<Exam> courseExamsObv = FXCollections.observableList(new ArrayList<>(courseExams));
        examsTableView.setItems(courseExamsObv);
    }

    @FXML
    public void okExamResponse(ActionEvent actionEvent) {
        if(titleTextField.getText().length() == 0) {
            GuiUtil.alert(Alert.AlertType.WARNING, "Specify exam title!");
            return;
        }
        if(proctorIDTextField.getText().length() == 0) {
            GuiUtil.alert(Alert.AlertType.WARNING, "Specify the Proctor ID!");
            return;
        } else {
            try {
                Integer.parseInt(proctorIDTextField.getText());
            } catch (NumberFormatException e) {
                GuiUtil.alert(Alert.AlertType.WARNING, "Proctor ID has to be a number!");
                return;
            }
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
        if(descriptionTextArea.getText().length()==0){
            GuiUtil.alert(Alert.AlertType.WARNING,"Specify the description");
        }
        LocalDate date = examDatePicker.getValue();
        System.out.println("LocalDate = " + date);
        System.out.println("year = " + date.getYear());
        System.out.println("month = " + date.getMonthValue());
        System.out.println("day = " + date.getDayOfMonth());

        int proctorId = Integer.parseInt(proctorIDTextField.getText());
        Date startTime = new Date(date.getYear()-1900,
                date.getMonthValue()-1,
                date.getDayOfMonth(),
                Integer.parseInt(startTimeHourTextField.getText()),
                Integer.parseInt(startTimeMinTextField.getText()));
        System.out.println(startTime);
        Date endTime = new Date(date.getYear()-1900,
                date.getMonthValue()-1,
                date.getDayOfMonth(),
                Integer.parseInt(endTimeHourTextField.getText()),
                Integer.parseInt(endTimeMinTextField.getText()));
        String examTitle = titleTextField.getText();
        String description=descriptionTextArea.getText();
        List<Question> questions = new ArrayList<>(questionsTableView.getItems());
        SetExamRequest newExam = new SetExamRequest(Main.getTeacherId(),
                this.courseId, proctorId, startTime, endTime, examTitle,description, questions);
        System.out.println("Here exam request set.");
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

            System.out.println("Sending request");
            Main.sendRequest(newExam);
            System.out.println("Waiting for response");
            SetExamResponse response = (SetExamResponse) Main.receiveResponse();
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
            else if(response.getStatus()==Status.PROCTOR_INVALID){
                GuiUtil.alert(Alert.AlertType.WARNING,"Invalid Proctor ID");
            }
            else if(response.getStatus()==Status.PROCTOR_UNAVAILABLE){
                GuiUtil.alert(Alert.AlertType.WARNING,"Proctor has a clashing exam");
            }
    }

    public void backResponse(ActionEvent actionEvent) {
        Main.chatVBox = null;
        Main.lastOpenCourseId = null;
        GuiUtil.goToHome((Stage) deleteButton.getScene().getWindow());
    }

    public void onChatClicked(Event event) {
        chatContainer.getChildren().clear();
        Main.sendRequest(new DisplayMessagesRequest(courseId));
        DisplayMessagesResponse displayMessagesResponse = (DisplayMessagesResponse) Main.receiveResponse();
        ArrayList<Message> messages = displayMessagesResponse.getMessages();

        for (Message message : messages) {
            if(message.getImage() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/SingleImageChatCardFXML.fxml"));
                try {
                    Node node = fxmlLoader.load();
                    SingleImageChatCardFXMLController singleImageChatCardFXMLController = fxmlLoader.getController();
                    BufferedImage bufferedImage=  ((ToolkitImage)message.getImage().getImage()).getBufferedImage();
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    singleImageChatCardFXMLController.imageView.setImage(image);
                    singleImageChatCardFXMLController.vBox.setAlignment(message.getSenderID().equals(Main.getTeacherId()) ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
                    singleImageChatCardFXMLController.nameLabel.setText(message.getSenderID().equals(Main.getTeacherId())?"Me":message.getSenderName());
                    if(message.getSenderID().equals(Main.getTeacherId()))
                        singleImageChatCardFXMLController.chatImageHBox.setAlignment(Pos.TOP_RIGHT);
                    singleImageChatCardFXMLController.timestampLabel.setText(message.getSentAt().toString());
                    singleImageChatCardFXMLController.nameHBox.backgroundProperty().set(new Background(new BackgroundFill(Color.web(
                            (message.getSenderID().equals(Main.getTeacherId())) ? Main.myColor : Main.otherColor),
                            CornerRadii.EMPTY,
                            Insets.EMPTY)));
                    Main.chatVBox.getChildren().add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/SingleChatCardFXML.fxml"));
                try {
                    Node node = fxmlLoader.load();
                    SingleChatCardFXMLController singleChatCardFXMLController = fxmlLoader.getController();
                    singleChatCardFXMLController.messageLabel.setText(message.getText());
                    singleChatCardFXMLController.messageLabel.setAlignment(message.getSenderID().equals(Main.getTeacherId()) ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
                    singleChatCardFXMLController.nameLabel.setText(message.getSenderID().equals(Main.getTeacherId())?"Me":message.getSenderName());
                    if(message.getSenderID().equals(Main.getTeacherId()))
                        singleChatCardFXMLController.chatCardHBox.setAlignment(Pos.TOP_RIGHT);
                    singleChatCardFXMLController.timestampLabel.setText(message.getSentAt().toString());
                    singleChatCardFXMLController.nameHBox.backgroundProperty().set(new Background(new BackgroundFill(Color.web(
                            (message.getSenderID().equals(Main.getTeacherId())) ? Main.myColor : Main.otherColor),
                            CornerRadii.EMPTY,
                            Insets.EMPTY)));
                    Main.chatVBox.getChildren().add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendButtonResponse() throws IOException {
        String text = sendTextField.getText().trim();
        BufferedImage bufferedImage = null;

        ImageIcon imageIcon = null;
        if(selectedFile != null) {
            bufferedImage = ImageIO.read(selectedFile);
            imageIcon = new ImageIcon(bufferedImage,null);
        }

        if(text == "" && imageIcon==null)
            return;
        sendTextField.clear();
        uploadImageButton.setText("Upload");
        Main.sendRequest(new Message(Main.getTeacherId(), Main.getTeacherName(), courseId, courseName,text, imageIcon,
                new Timestamp(System.currentTimeMillis()), true));
        SendMessageResponse sendMessageResponse = (SendMessageResponse) Main.receiveResponse();
        assert sendMessageResponse != null;

        selectedFile = null;
        System.out.println(sendMessageResponse.getResponse());
    }

    public void uploadButtonResponse(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image files","*.png","*.jpg","*.jpeg"));
        selectedFile = fc.showOpenDialog(null);
        if(selectedFile != null)
            uploadImageButton.setText(selectedFile.getName());
    }

    public void addStudentButtonResponse(ActionEvent actionEvent) {
        AddStudentRequest addStudentRequest=new AddStudentRequest(courseId,addStudentTextField.getText());
        Main.sendRequest(addStudentRequest);
        AddStudentResponse addStudentResponse=(AddStudentResponse)Main.receiveResponse();
        assert addStudentResponse != null;
        if(addStudentResponse.getStatus()==Status.STUDENT_ADDED){
            GuiUtil.alert(Alert.AlertType.INFORMATION,"Student added successfully");
            populateCourseStudentsTable();
        }else if(addStudentResponse.getStatus()==Status.REGISTRATION_NUMBER_INVALID){
            GuiUtil.alert(Alert.AlertType.ERROR,"Invalid Registration Number");
        }
    }
}
