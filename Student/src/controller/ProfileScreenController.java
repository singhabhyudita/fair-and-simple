package controller;

import entity.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import request.*;
import response.*;
import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


public class ProfileScreenController implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //This will be called before Profile Screen is loaded and when refresh is called
    public void refreshButtonResponse() {
        setUpcomingExamsList();
        setExamsHistory();
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
        if(getProfilePicResponse != null) {
            bufferedImage=  ((ToolkitImage)getProfilePicResponse.getImageIcon().getImage()).getBufferedImage();
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            profilePicImageView.setImage(image);
            changeProfilePicImageView.setImage(image);
        }
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
    public FlowPane examHistoryContainer;

    private void setExamsHistory() {
        //Sending request to server to fetch user's exam history
        examHistoryContainer.getChildren().clear();
        ExamsHistoryRequest examsHistoryRequest = new ExamsHistoryRequest();
        Main.sendRequest(examsHistoryRequest);
        ExamsHistoryResponse examsHistoryResponse = (ExamsHistoryResponse) Main.getResponse();

        ArrayList<Exam> exams = examsHistoryResponse.getExamsList();
        for (Exam exam : exams) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SingleExamHistoryCardFXML.fxml"));
            try {
                Node node = fxmlLoader.load();
                SingleExamHistoryCardFXMLController singleExamHistoryCardFXMLController = fxmlLoader.getController();
                singleExamHistoryCardFXMLController.setCourseLabel(exam.getCourseName());
                singleExamHistoryCardFXMLController.setMarksLabel(exam.getMaxMarks() + "/" + exam.getMaxMarks());
                singleExamHistoryCardFXMLController.setTitleLabel(exam.getTitle());
                examHistoryContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Show the upcoming exams of the user
    @FXML
    public FlowPane examListContainer;


    private void setUpcomingExamsList() {
        //Sending request to server to fetch user's upcoming exams
        examListContainer.getChildren().clear();
        UpcomingExamsRequest upcomingExamsRequest = new UpcomingExamsRequest();
        Main.sendRequest(upcomingExamsRequest);
        UpcomingExamsResponse upcomingExamsResponse = (UpcomingExamsResponse) Main.getResponse();

        //Setting list of exams as Observable list in Exams Table
        assert upcomingExamsResponse != null;
        ArrayList<Exam> exams = upcomingExamsResponse.getExamsList();
        for(Exam exam : exams){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/QuizCardLayoutFXML.fxml"));
            try {
                Node node = fxmlLoader.load();
                QuizCardLayoutFXMLController quizCardLayoutFXMLController = fxmlLoader.getController();
                quizCardLayoutFXMLController.setExam(exam);
                quizCardLayoutFXMLController.setNoq(exam.getMaxMarks() + "");
                quizCardLayoutFXMLController.setCourse(exam.getCourseName());
                examListContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Show the course the user is registered for
    @FXML
    public FlowPane courseContainer;

    private void setCoursesList() {
        //Sending request to server to fetch user's enrolled courses
        courseContainer.getChildren().clear();
        CoursesListRequest coursesListRequest = new CoursesListRequest();
        Main.sendRequest(coursesListRequest);
        CoursesListResponse coursesListResponse = (CoursesListResponse) Main.getResponse();

        ArrayList <Course> courses = coursesListResponse.getCoursesList();
        for(Course course : courses){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/CourseCardLayoutFXML.fxml"));
            try {
                Node node = fxmlLoader.load();
                CourseCardLayoutFXMLController courseCardLayoutFXMLController = fxmlLoader.getController();
                courseCardLayoutFXMLController.setAboutLabel(course.getCourseDescription());
                courseCardLayoutFXMLController.setProfessorNameLabel(course.getTeacherName());
                courseCardLayoutFXMLController.setCourseLabel(course.getCourseName());
                courseCardLayoutFXMLController.setName(this.name);
                courseCardLayoutFXMLController.setCourse(course);
                courseContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String name;
    @FXML
    public Label heyNameLabel;

    public void first(String name) {
        this.name=name;
        heyNameLabel.setText("Hey, "+name);
        System.out.println("inside the first method after login trying to create chat socket");
        setCoursesList();
       // setNotificationsList();
        setUpcomingExamsList();
        setExamsHistory();
        setProfilePic();
    }

    public void setNotificationsList(){

    }

    @FXML
    public VBox notificationContainer;

    public void onNotificationsClicked(Event event) {
        Main.notificationVbox=notificationContainer;
        notificationContainer.getChildren().clear();
        Main.sendRequest(new GetNotificationRequest());
        System.out.println("notif request sent");
        GetNotificationResponse getNotificationResponse=(GetNotificationResponse)Main.getResponse();
        System.out.println("notif response received");
        assert getNotificationResponse != null;
        System.out.println("response is "+getNotificationResponse);
        ArrayList<Notification>notificationArrayList=getNotificationResponse.getNotificationArrayList();

        for (Notification notification:notificationArrayList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SingleNotificationCardFXML.fxml"));
            try {
                Node node = fxmlLoader.load();
                SingleNotificationCardFXMLController singleNotificationCardFXMLController = fxmlLoader.getController();
                singleNotificationCardFXMLController.courseLabel.setText(notification.getCourseName());
                singleNotificationCardFXMLController.messageLabel.setText(notification.getText());
                singleNotificationCardFXMLController.timestampLabel.setText(notification.getSentAt().toString());
                notificationContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
