package Controller;

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
import javafx.scene.image.ImageView;
import Response.*;
import Request.*;
import Classes.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class ProfileScreenController implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //This will be called before Profile Screen is loaded and when refresh is called
    public void refreshButtonResponse() {
        setUpcomingExamsList();
        setExamsHistoryTableView();
        setCoursesList();
        setProfilePic();
    }

    //Setting profile pic on top left and in change profile pic tab
    @FXML
    private ImageView profilePicImageView;
    @FXML
    private ImageView changeProfilePicImageView;

    private void setProfilePic() {
        GetProfilePicRequest getProfilePicRequest = new GetProfilePicRequest();
        Main.sendRequest(getProfilePicRequest);
        GetProfilePicResponse getProfilePicResponse = (GetProfilePicResponse) Main.getResponse();
        profilePicImageView.setImage(getProfilePicResponse.getImage());
        changeProfilePicImageView.setImage(getProfilePicResponse.getImage());
    }

    // Join a course
    @FXML
    private TextField enterCourseCodeTextField;

    public void joinCourseButtonResponse(ActionEvent actionEvent) {
        String courseCode = enterCourseCodeTextField.getText().trim();
        JoinCourseRequest joinCourseRequest = new JoinCourseRequest(courseCode);
        JoinCourseResponse joinCourseResponse = (JoinCourseResponse) Main.getResponse();
        if(joinCourseResponse.getResponse() == "Successful") {
            JOptionPane.showMessageDialog(null,"Successfully joined course.");
            //Redirect to the team joined
        }
        else {
            JOptionPane.showMessageDialog(null,"Invalid course code.");
        }
    }

    //Change password
    @FXML
    private PasswordField oldPasswordTextField;
    @FXML
    private PasswordField newPasswordTextField;
    @FXML
    private PasswordField confirmNewPasswordTextField;

    public void changePasswordButtonResponse(ActionEvent actionEvent) {
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmedNewPassword = confirmNewPasswordTextField.getText();

        if(newPassword == confirmedNewPassword) {
            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword,newPassword);
            ChangePasswordResponse changePasswordResponse = (ChangePasswordResponse) Main.getResponse();
            if(changePasswordResponse.getResponse() == "Successful") {
                JOptionPane.showMessageDialog(null,"Password changed successfully!");
                //Redirect to the team joined
            }
            else {
                JOptionPane.showMessageDialog(null,"Some error occurred.");
            }
        }
        else {
            JOptionPane.showMessageDialog(null,"New password fields don't match.");
        }
    }

    //Changing profile picture
    @FXML
    private Button selectImageButton;
    File selectedFile;
        //Select the image to set
    public void selectImageButtonResponse(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image files","*.png","*.jpg","*.jpeg"));
        selectedFile = fc.showOpenDialog(null);
        selectImageButton.setText(selectedFile.getName());
    }
        //Send request to server to set it
    public void confirmPicChangeButtonResponse(ActionEvent actionEvent) {
        try {
            FileInputStream fis = new FileInputStream(selectedFile);
            ChangeProfilePicRequest ChangeProfilePicRequest = new ChangeProfilePicRequest(fis);
            ChangeProfilePicResponse ChangeProfilePicResponse = (ChangeProfilePicResponse) Main.getResponse();
            if(ChangeProfilePicResponse.getResponse() == "Successful") {
                JOptionPane.showMessageDialog(null,"Profile picture changed successfully!");
                setProfilePic();
            }
            else {
                JOptionPane.showMessageDialog(null,"Some error occurred.");
            }
        }
        catch(FileNotFoundException fileNotFoundException) {
            JOptionPane.showMessageDialog(null,"Please select a valid image first!");
        }
    }

    //Logout
    public void logOutButtonResponse(ActionEvent actionEvent) {
        LogOutRequest logOutRequest = new LogOutRequest();
        LogOutResponse logOutResponse = (LogOutResponse)Main.getResponse();
        if(logOutResponse.getResponse() == "Successful") {
            //Load the login screen
        }
        else {
            JOptionPane.showMessageDialog(null,"LogOut failed. Please try again!");
        }
    }

    //Show the exams history of the user
    @FXML
    private TableColumn historyCourseNameTableColumn;
    @FXML
    private TableColumn historyTitleTableColumn;
    @FXML
    private TableColumn historyMarksTableColumn;
    @FXML
    private TableColumn historyMaxMarksTableColumn;
    @FXML
    private TableView examsHistoryTableView;
    public ObservableList<Exam> observableExamsHistoryList;

    private void setExamsHistoryTableView() {
        historyCourseNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("courseName")
        );
        historyTitleTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("title")
        );
        historyMarksTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("marks")
        );
        historyMaxMarksTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("maxMarks")
        );

        //Sending request to server to fetch user's exam history
        ExamsHistoryRequest examsHistoryRequest = new ExamsHistoryRequest();
        Main.sendRequest(examsHistoryRequest);
        ExamsHistoryResponse examsHistoryResponse = (ExamsHistoryResponse) Main.getResponse();

        //Setting list of exams as Observable list in Exams Table
        observableExamsHistoryList = FXCollections.observableList(examsHistoryResponse.getExamsList());
        examsHistoryTableView.setItems(observableExamsHistoryList);
    }

    //Show the upcoming exams of the user
    @FXML
    private TableColumn upcomingCourseNameTableColumn;
    @FXML
    private TableColumn upcomingTitleTableColumn;
    @FXML
    private TableColumn upcomingTimeTableColumn;
    @FXML
    private TableColumn upcomingMaxMarksTableColumn;
    @FXML
    private TableView upcomingExamsTableView;
    public ObservableList<Exam> observableUpcomingExamsList;

    private void setUpcomingExamsList() {
        upcomingCourseNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("courseName")
        );
        upcomingTitleTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, String>("title")
        );
        upcomingTimeTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, Time>("startTime")
        );
        upcomingMaxMarksTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, Integer>("maxMarks")
        );

        //Sending request to server to fetch user's upcoming exams
        UpcomingExamsRequest upcomingExamsRequest = new UpcomingExamsRequest();
        Main.sendRequest(upcomingExamsRequest);
        UpcomingExamsResponse upcomingExamsResponse = (UpcomingExamsResponse) Main.getResponse();

        //Setting list of exams as Observable list in Exams Table
        observableUpcomingExamsList = FXCollections.observableList(upcomingExamsResponse.getExamsList());
        upcomingExamsTableView.setItems(observableUpcomingExamsList);
    }


    //Show the course the user is registered for
    @FXML
    private TableColumn courseNameTableColumn;
    @FXML
    private TableColumn professorNameTableColumn;
    @FXML
    private TableView coursesTableView;
    public ObservableList<Course> observableCoursesList;

    private void setCoursesList() {
        courseNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<Course, String>("courseName")
        );
        professorNameTableColumn.setCellValueFactory(
                new PropertyValueFactory<Course, String>("professorName")
        );

        //Sending request to server to fetch user's upcoming exams
        CoursesListRequest coursesListRequest = new CoursesListRequest();
        Main.sendRequest(coursesListRequest);
        CoursesListResponse coursesListResponse = (CoursesListResponse) Main.getResponse();

        //Setting list of exams as Observable list in Exams Table
        observableCoursesList = FXCollections.observableList(coursesListResponse.getCoursesList());
        coursesTableView.setItems(observableCoursesList);
    }
}

