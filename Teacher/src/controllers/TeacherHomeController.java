package controllers;

import entity.Course;
import entity.Exam;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.GuiUtil;
import main.Main;
import request.*;
import response.*;
import sun.awt.image.ToolkitImage;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherHomeController {
    @FXML
    public ImageView profilePicImageView;
    @FXML
    public Button changePasswordButton;
    @FXML
    public Label heyNameLabel;
    @FXML
    public TextField courseSearchTextField;
    @FXML
    public TableView<Course> coursesTableView;
    @FXML
    public TableColumn<Course, String> courseTableColumn;
    @FXML
    public TextField courseNameTextField;
    @FXML
    public TextArea courseDescriptionTextArea;
    @FXML
    public TextField courseCodeTextField;
    @FXML
    public Button createCourseButton;
    @FXML
    public TextField examHistorySearchBar;
    @FXML
    public TableView<Exam> resultExamsTableView;
    @FXML
    public TableColumn<Exam, String> examResultCourseTableColumn;
    @FXML
    public TableColumn<Exam, String> examsResultTitleTableColumn;
    @FXML
    public TableColumn<Exam, Date> examsResultDateTableColumn;
    @FXML
    public TableView particularResultTableView;
    @FXML
    public TableColumn particularResultRankTableColumn;
    @FXML
    public TableColumn particularResultRegistrationNumberTableColumn;
    @FXML
    public TableColumn particularResultNameTableColumn;
    @FXML
    public TableColumn particularResultMarksTableColumn;
    @FXML
    public TextField upcomingExamsSearchBar;
    @FXML
    public TableView<Exam> upcomingExamsTableView;
    @FXML
    public TableColumn<Exam, String> upcomingCourseNameTableColumn;
    @FXML
    public TableColumn<Exam, String> upcomingTitleTableColumn;
    @FXML
    public TableColumn<Exam, Date> upcomingTimeTableColumn;
    @FXML
    public TableColumn<Exam, Integer> upcomingMaxMarksTableColumn;
    @FXML
    public TextField proctoringDutyExamsSearchBar;
    @FXML
    public TableColumn<Exam, Button> upcomingCheckButtonTableColumn;
    @FXML
    public TableView<Exam> proctoringDutyExamsTableView;
    @FXML
    public TableColumn<Exam, String> proctoringDutyCourseNameTableColumn;
    @FXML
    public TableColumn<Exam, String> proctoringDutyTitleTableColumn;
    @FXML
    public TableColumn<Exam, Date> proctoringDutyTimeTableColumn;
    @FXML
    public PasswordField oldPasswordTextField;
    @FXML
    public PasswordField newPasswordTextField;
    @FXML
    public PasswordField confirmNewPasswordTextField;
    @FXML
    public Button selectImageButton;
    @FXML
    public Button confirmPicChangeButton;
    @FXML
    public ImageView changeProfilePicImageView;
    private TeacherExamResponse teacherExamResponse;
    private File selectedFile;

    @FXML
    public void createExamResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void upcomingExamsResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void changePasswordResponse(ActionEvent actionEvent) {
        if(!newPasswordTextField.getText().equals(confirmNewPasswordTextField.getText())) {
            GuiUtil.alert(Alert.AlertType.WARNING, "New password and confirm password don't match.");
            return;
        }
        changePasswordButton.setDisable(true);
        TeacherChangePasswordRequest request = new TeacherChangePasswordRequest(Main.getTeacherId(),
                oldPasswordTextField.getText(), newPasswordTextField.getText());
        Main.sendRequest(request);
        TeacherChangePasswordResponse response = (TeacherChangePasswordResponse) Main.receiveResponse();
        if(response == null || response.getStatus() == -1) {
            GuiUtil.alert(Alert.AlertType.ERROR, "Could not change the password");
        } else {
            GuiUtil.alert(Alert.AlertType.INFORMATION, "Password changed successfully!");
            newPasswordTextField.clear();
            oldPasswordTextField.clear();
            confirmNewPasswordTextField.clear();
            changePasswordButton.setDisable(false);
        }
    }

    @FXML
    public void changeProfilePictureResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void logOutResponse(ActionEvent actionEvent) {
        System.out.println("Log out button clicked!!");
        Main.sendRequest(new LogOutRequest());
        Main.receiveResponse();
        Main.setTeacherId("");
        System.out.println("Login screen being loaded");
        FXMLLoader loader=new FXMLLoader(getClass().getResource("../views/TeacherLoginView.fxml"));
        Stage stage= (Stage) changePasswordButton.getScene().getWindow();
        Scene scene = null;
        try {
            scene=new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Setting login screen");
        stage.setScene(scene);
        System.out.println("Login screen set");
    }

    @FXML
    public void onCourseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2)
        {
            Course selectedCourse = coursesTableView.getSelectionModel().getSelectedItem();
            String courseTitle = selectedCourse.getCourseName();
            String courseId = selectedCourse.getCourseId();
            System.out.println("Clicked = " + courseId);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CourseView.fxml"));
            Stage stage = (Stage) changePasswordButton.getScene().getWindow();
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.setTitle(courseTitle);
            CourseController controller = loader.getController();
            List<Exam> courseExam = getExamsForCourse(selectedCourse.getCourseId());
            controller.callFirst(selectedCourse.getCourseId(), courseExam);
        }
    }

    private List<Exam> getExamsForCourse(String courseId) {
        List<Exam> ret = new ArrayList<>();
        for(Exam e : teacherExamResponse.getExams())
            if(e.getCourseId().equals(courseId))
                ret.add(e);
        return ret;
    }

    public void callFirst() {
        heyNameLabel.setText("Hey " + Main.getTeacherName() + "!");
        populateTeacherCourses();
        populateExamTables();
        populateProctoringDutyExamTable();
        setProfilePic();
    }
    @FXML
    public void createCourseButtonResponse(ActionEvent actionEvent) {
        if(courseNameTextField.getText() == null || courseNameTextField.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter a non-empty course name.");
            alert.showAndWait();
        } else {
            courseNameTextField.setEditable(false);
            courseDescriptionTextArea.setEditable(false);
            createCourseButton.setDisable(true);
            CreateCourseRequest request = new CreateCourseRequest(Main.getTeacherId(), courseDescriptionTextArea.getText(), courseNameTextField.getText());
            Main.sendRequest(request);
            CreateCourseResponse response = (CreateCourseResponse) Main.receiveResponse();
            System.out.println("Response = " + response);
            if(response == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could Not create a course. Please try again.");
                alert.showAndWait();
                courseNameTextField.setEditable(true);
                courseDescriptionTextArea.setEditable(true);
                createCourseButton.setDisable(false);
            } else {
                courseCodeTextField.setText(response.getTeamCode());
            }
        }
    }

    private void populateTeacherCourses() {
        System.out.println("The thread is " + Thread.currentThread());
        System.out.println("Called first");
        courseTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        System.out.println("Values set");
        System.out.println("Inside courses request thread" + Thread.currentThread());
        TeacherCoursesRequest request = new TeacherCoursesRequest(Main.getTeacherId());
        Main.sendRequest(request);
        TeacherCoursesResponse response = (TeacherCoursesResponse) Main.receiveResponse();
        System.out.println("Fetched courses response");
        if(response == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fetch your courses. Click OK to exit the application");
            alert.showAndWait();
            System.exit(0);
        } else {
            System.out.println("Creating courses list");
            ObservableList<Course> courseList = FXCollections.observableList(response.getCourses());
            System.out.println("List created. Now setting into table.");
            coursesTableView.setItems(courseList);
            System.out.println("List set into table.");
        }
    }

    private void populateExamTables() {
        upcomingCourseNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        upcomingTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        upcomingTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        upcomingMaxMarksTableColumn.setCellValueFactory(new PropertyValueFactory<>("maxMarks"));
        examsResultTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        examsResultDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        examResultCourseTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        TeacherExamRequest request = new TeacherExamRequest(Main.getTeacherId(), false);
        Main.sendRequest(request);
        teacherExamResponse = (TeacherExamResponse) Main.receiveResponse();
        if(teacherExamResponse == null) GuiUtil.alert(Alert.AlertType.ERROR, "Could not fetch your exams. Might be a server error.");
        else {
            ObservableList<Exam> upcomingExams = FXCollections.observableList(teacherExamResponse.getExams().stream()
                    .filter((Exam e) -> e.getDate().after(new Date()))
                    .collect(Collectors.toList()));
            ObservableList<Exam> pastExams = FXCollections.observableList(teacherExamResponse.getExams().stream()
                    .filter((Exam e) -> e.getDate().before(new Date()))
                    .collect(Collectors.toList()));
            upcomingExamsTableView.setItems(upcomingExams);
            resultExamsTableView.setItems(pastExams);
        }
    }

    private void populateProctoringDutyExamTable() {
        ProctoringDutyRequest request = new ProctoringDutyRequest();
        Main.sendRequest(request);
        ProctoringDutyResponse response = (ProctoringDutyResponse) Main.receiveResponse();
        proctoringDutyCourseNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        proctoringDutyTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        proctoringDutyTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        if(response != null){
            ObservableList<Exam> examsToProctor = FXCollections.observableList(response.getExams());
            proctoringDutyExamsTableView.setItems(examsToProctor);
        }
    }

    public void changePasswordButtonResponse(ActionEvent actionEvent) {
    }

    public void selectImageButtonResponse(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image files","*.png","*.jpg","*.jpeg"));
        selectedFile = fc.showOpenDialog(null);
        selectImageButton.setText(selectedFile.getName());
    }

    public void confirmPicChangeButtonResponse(ActionEvent actionEvent) {
        try {
            FileInputStream fis = new FileInputStream(selectedFile);
            byte[] imageArray  = new byte[(int) selectedFile.length()];
            fis.read(imageArray);
            fis.close();
            ChangeTeacherProfilePicRequest changeTeacherProfilePicRequest = new ChangeTeacherProfilePicRequest(imageArray);
            Main.sendRequest(changeTeacherProfilePicRequest);
            System.out.println("profile pic Request sent ");
            ChangeTeacherProfilePicResponse changeProfilePicResponse = (ChangeTeacherProfilePicResponse) Main.receiveResponse();
            System.out.println("response profile pic "+ changeProfilePicResponse);
            assert changeProfilePicResponse != null;
            if(changeProfilePicResponse.getResponse().equals("Successful")) {
                JOptionPane.showMessageDialog(null,"Profile picture changed successfully!");
                setProfilePic();
            }
            else {
                JOptionPane.showMessageDialog(null,"Some error occurred.");
            }
        }
        catch(FileNotFoundException fileNotFoundException) {
            JOptionPane.showMessageDialog(null,"Please select a valid image first!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setProfilePic();
    }

    private void setProfilePic() {
        GetTeacherProfilePicRequest getTeacherProfilePicRequest = new GetTeacherProfilePicRequest();
        Main.sendRequest(getTeacherProfilePicRequest);
        GetTeacherProfilePicResponse getProfilePicResponse = (GetTeacherProfilePicResponse) Main.receiveResponse();
        System.out.println("Image input stream received "+getProfilePicResponse);
        BufferedImage bufferedImage;
        Image image;
        if(getProfilePicResponse != null && getProfilePicResponse.getImageIcon() != null) {
            bufferedImage=  ((ToolkitImage)getProfilePicResponse.getImageIcon().getImage()).getBufferedImage();
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            profilePicImageView.setImage(image);
            changeProfilePicImageView.setImage(image);
        }
    }

    public void refreshButtonResponse(ActionEvent actionEvent) {
        callFirst();
    }

    public void clearResponse(ActionEvent actionEvent) {
        courseNameTextField.clear();
        courseDescriptionTextArea.clear();
        courseCodeTextField.clear();
    }

    public void onExamToProctorClick(MouseEvent mouseEvent) {
        System.out.println("Yoyo");
        if(mouseEvent.getClickCount() == 2) {
            Exam exam = proctoringDutyExamsTableView.getSelectionModel().getSelectedItem();
            Timestamp startTime = exam.getDate();
            if(startTime.getTime() - (new Date().getTime()) <= 15*60*1000) { // 15 min into ms.
                DatagramSocket videoFeedSocket = null;
                try {
                    videoFeedSocket = new DatagramSocket(0);
                    ProctoringRequest request = new ProctoringRequest(exam.getExamId(), videoFeedSocket.getLocalPort(), InetAddress.getLocalHost());
                    Main.sendRequest(request);
                    ProctoringResponse response = (ProctoringResponse) Main.receiveResponse();
                    if(response.isSetupDone()) {
                        try {
                            FXMLLoader loader = new FXMLLoader(Main.class.getResource("../views/ProctorView.fxml"));
                            Scene scene = new Scene(loader.load());
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setTitle("Proctoring - Course: " + exam.getCourseName() + " Exam: " + exam.getTitle());
                            stage.setMaximized(true);
                            stage.show();
                            ProctorController controller = loader.getController();
                            controller.callFirst(videoFeedSocket, response.getStudents());
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        GuiUtil.alert(Alert.AlertType.ERROR, "Could not start proctoring due to a server error!");
                    }
                } catch (SocketException | UnknownHostException e) {
                    e.printStackTrace();
                }
            } else {
                GuiUtil.alert(Alert.AlertType.WARNING, "You can access the proctoring screen only when there are less than 15 minutes remaining for the commencement of the exam.");
            }
        }
    }
}
