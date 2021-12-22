package controller;

import entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import request.*;
import response.*;
import sun.awt.image.ToolkitImage;
import util.ChatUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
        System.out.println("Image input stream received "+getProfilePicResponse);
        BufferedImage bufferedImage;
        Image image;
        assert getProfilePicResponse != null;
        bufferedImage=  ((ToolkitImage)getProfilePicResponse.getImageIcon().getImage()).getBufferedImage();
        image = SwingFXUtils.toFXImage(bufferedImage, null);
        profilePicImageView.setImage(image);
        changeProfilePicImageView.setImage(image);
    }

    // Join a course
    @FXML
    private TextField enterCourseCodeTextField;
    @FXML
    public Button joinCourseButton;

    public void joinCourseButtonResponse() {
        String courseCode = enterCourseCodeTextField.getText().trim();
        Main.sendRequest(new JoinCourseRequest(courseCode));
        System.out.println("Join course request sent");
        JoinCourseResponse joinCourseResponse = (JoinCourseResponse) Main.getResponse();
        assert joinCourseResponse != null;
        if(joinCourseResponse.getResponse().equals("Successful")) {
            JOptionPane.showMessageDialog(null,"Successfully joined course.");
            //Redirect to the team joined
            FXMLLoader loader= new FXMLLoader(getClass().getResource("../fxml/CourseTabPane.fxml"));
            Stage stage=(Stage)joinCourseButton.getScene().getWindow();
            Scene scene=null;
            try {
                 scene=new Scene(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.setTitle("Course");
            CourseTabPaneController courseTabPaneController=loader.getController();
            try {
                courseTabPaneController.first(joinCourseResponse.getCourseID(),name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    public void changePasswordButtonResponse() {
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmedNewPassword = confirmNewPasswordTextField.getText();

        if(newPassword.equals(confirmedNewPassword)) {
            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword,newPassword);
            System.out.println("change password request sent");
            Main.sendRequest(changePasswordRequest);
            ChangePasswordResponse changePasswordResponse = (ChangePasswordResponse) Main.getResponse();
            assert changePasswordResponse != null;
            if(changePasswordResponse.getResponse().equals("Successful")) {
                JOptionPane.showMessageDialog(null,"Password changed successfully!");
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
    public void selectImageButtonResponse() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image files","*.png","*.jpg","*.jpeg"));
        selectedFile = fc.showOpenDialog(null);
        selectImageButton.setText(selectedFile.getName());
    }
        //Send request to server to set it
    public void confirmPicChangeButtonResponse() {
        try {
            FileInputStream fis = new FileInputStream(selectedFile);
            byte[] imageArray  = new byte[(int) selectedFile.length()];
            fis.read(imageArray);
            fis.close();
            ChangeProfilePicRequest changeProfilePicRequest = new ChangeProfilePicRequest(imageArray);
            Main.sendRequest(changeProfilePicRequest);
            System.out.println("profile pic Request sent ");
            ChangeProfilePicResponse changeProfilePicResponse = (ChangeProfilePicResponse) Main.getResponse();
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
    }

    //Logout
    @FXML
    private Button logOutButton;
    public void logOutButtonResponse() {
        LogOutRequest logOutRequest = new LogOutRequest();
        Main.sendRequest(logOutRequest);
        LogOutResponse logOutResponse = (LogOutResponse)Main.getResponse();
        assert logOutResponse != null;
        System.out.println("Response received ");
        if(logOutResponse.getResponse().equals("Successful")) {
            System.out.println("Logout "+logOutResponse.getResponse());
            FXMLLoader loader=new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
            Stage stage=(Stage)logOutButton.getScene().getWindow();
            try {
                Scene scene=new Scene(loader.load());
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Login");
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
                new PropertyValueFactory<Exam, Time>("date")
        );
        upcomingMaxMarksTableColumn.setCellValueFactory(
                new PropertyValueFactory<Exam, Integer>("maxMarks")
        );

        //Sending request to server to fetch user's upcoming exams
        UpcomingExamsRequest upcomingExamsRequest = new UpcomingExamsRequest();
        Main.sendRequest(upcomingExamsRequest);
        UpcomingExamsResponse upcomingExamsResponse = (UpcomingExamsResponse) Main.getResponse();

        //Setting list of exams as Observable list in Exams Table
        assert upcomingExamsResponse != null;
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
                new PropertyValueFactory<Course, String>("courseDescription")
        );

        //Sending request to server to fetch user's enrolled courses
        CoursesListRequest coursesListRequest = new CoursesListRequest();
        Main.sendRequest(coursesListRequest);
        CoursesListResponse coursesListResponse = (CoursesListResponse) Main.getResponse();

        //Setting list of courses as observable
        System.out.println("Courses list response received, trying to populate the table");
        assert coursesListResponse != null;
        observableCoursesList = FXCollections.observableList(coursesListResponse.getCoursesList());
        coursesTableView.setItems(observableCoursesList);
    }

    public void onCourseClicked(MouseEvent mouseEvent) {
        FXMLLoader courseLoader=new FXMLLoader(getClass().getResource("../fxml/CourseTabPane.fxml"));
        Course course= (Course) coursesTableView.getSelectionModel().getSelectedItem();
        Scene scene=null;
        try {
             scene=new Scene(courseLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) selectImageButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Course");
        CourseTabPaneController courseTabPaneController=courseLoader.getController();
        try {
            System.out.println("Trying to load course");
            courseTabPaneController.first(course.getCourseId(),name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private String name;
    @FXML
    public Label heyNameLabel;

    public void first(String name) {
        this.name=name;
        heyNameLabel.setText("Hey, "+name);
        setCoursesList();
        setUpcomingExamsList();
        setExamsHistoryTableView();
        setProfilePic();
    }

    public void onExamClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2)
        {
            Exam exam = (Exam)upcomingExamsTableView.getSelectionModel().getSelectedItem();
            if(exam.getDate().getTime() - (new Date()).getTime() > 0)
            {
                GuiUtil.alert(Alert.AlertType.WARNING,"Exam hasn't started yet!");
            }
            else
            {
                GetQuestionsResponse response = getData(exam);
                if(response.getProctorPort() == -1) {
                    GuiUtil.alert(Alert.AlertType.ERROR, "Exam will start only after the proctor joins!");
                    return;
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/QuestionsScreenFXML.fxml"));
                Stage currentStage=(Stage)heyNameLabel.getScene().getWindow();
                Scene scene=null;

                try {
                    scene=new Scene(fxmlLoader.load());
                    QuestionsScreenController questionsScreenController= fxmlLoader.getController();
                    questionsScreenController.setQuiz(exam);
                    questionsScreenController.setData(response.getProctorPort(), response.getQuestionsList());
                    currentStage.setScene(scene);
                    currentStage.setTitle(exam.getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

