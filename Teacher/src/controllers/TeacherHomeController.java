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
import request.TeacherCoursesRequest;
import response.Course;
import response.TeacherCoursesResponse;

import java.io.IOException;
import java.util.Optional;

public class TeacherHomeController {
    @FXML
    public Button resultsButton;
    @FXML
    public Button createTeamButton;
    @FXML
    public ImageView profilePicImageView;
    @FXML
    public Button upcomingExamsButton;
    @FXML
    public Button changePasswordButton;
    @FXML
    public Button changeProfilePicButton;
    @FXML
    public Button logOutButton;
    @FXML
    public Label heyNameLabel;
    @FXML
    public TextField searchTextField;
    @FXML
    public TableView<Course> coursesTableView;
    @FXML
    public TableColumn<Course, String> courseTableColumn;

    @FXML
    public void resultsResponse(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ResultsView.fxml"));
        Stage stage = (Stage) upcomingExamsButton.getScene().getWindow();
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
    public void createTeamResponse(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CreateTeamView.fxml"));
        Stage stage = (Stage) upcomingExamsButton.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Create New Team");
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
    public void clickItem(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2)
        {
            Course selectedCourse = coursesTableView.getSelectionModel().getSelectedItem();
            String courseTitle = selectedCourse.getCourseTitle();
            String courseId = selectedCourse.getCourseId();
            System.out.println("Clicked = " + courseId);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CourseView.fxml"));
            Stage stage = (Stage) upcomingExamsButton.getScene().getWindow();
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
        courseTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
        Platform.runLater(() -> {
            TeacherCoursesRequest request = new TeacherCoursesRequest(TeacherApplication.getTeacherId());
            TeacherApplication.sendRequest(request);
            TeacherCoursesResponse response = (TeacherCoursesResponse) TeacherApplication.receiveResponse();
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
}
