package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.TeacherApplication;
import request.CreateTeamRequest;
import request.TeacherCoursesRequest;
import response.Course;
import response.CreateTeamResponse;
import response.TeacherCoursesResponse;

import java.io.IOException;

public class TeacherHomeController {
    @FXML
    public ImageView profilePicImageView;
    @FXML
    public Button changePasswordButton;
    @FXML
    public Button logOutButton;
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
    public void resultsResponse(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ResultsView.fxml"));
        Stage stage = (Stage) changePasswordButton.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Results");
        ResultsController controller = loader.getController();
        controller.callFirst();
    }

    @FXML
    public void createExamResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void upcomingExamsResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void changePasswordResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void changeProfilePictureResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void logOutResponse(ActionEvent actionEvent) {
    }

    @FXML
    public void onCourseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2)
        {
            Course selectedCourse = coursesTableView.getSelectionModel().getSelectedItem();
            String courseTitle = selectedCourse.getCourseTitle();
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
            controller.callFirst(selectedCourse.getCourseId());
        }
    }

    public void callFirst() {
        System.out.println("The thread is " + Thread.currentThread());
        System.out.println("Called first");
        courseTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
        System.out.println("Values set");
        Platform.runLater(() -> {
            System.out.println("Inside courses request thread" + Thread.currentThread());
            TeacherCoursesRequest request = new TeacherCoursesRequest(TeacherApplication.getTeacherId());
            TeacherApplication.sendRequest(request);
            TeacherCoursesResponse response = (TeacherCoursesResponse) TeacherApplication.receiveResponse();
            System.out.println("Fetched courses response");
            if(response == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fetch your courses. Click OK to exit the application");
                alert.showAndWait();
                System.exit(0);
            } else {
                ObservableList<Course> courseList = FXCollections.observableList(response.getCourses());
                coursesTableView.setItems(courseList);
            }
        });
    }

    public void createCourseButtonResponse(ActionEvent actionEvent) {
        if(courseNameTextField.getText() == null || courseNameTextField.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter a non-empty course name.");
            alert.showAndWait();
        } else {
            courseNameTextField.setEditable(false);
            courseDescriptionTextArea.setEditable(false);
            createCourseButton.setDisable(true);
            Platform.runLater(() -> {
                CreateTeamRequest request = new CreateTeamRequest(TeacherApplication.getTeacherId(), courseDescriptionTextArea.getText(), courseNameTextField.getText());
                TeacherApplication.sendRequest(request);
                CreateTeamResponse response = (CreateTeamResponse) TeacherApplication.receiveResponse();
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
            });
        }
    }
}
