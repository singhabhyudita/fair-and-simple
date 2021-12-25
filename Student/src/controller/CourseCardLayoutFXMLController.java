package controller;

import entity.Course;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseCardLayoutFXMLController {
    @FXML
    public Label courseLabel;
    @FXML
    public Label aboutLabel;

    public void setCourse(Course course) {
        this.course = course;
    }

    private Course course;

    public void setCourseLabel(String course) {
        this.courseLabel.setText(course);
    }

    public void setAboutLabel(String about) {
        this.aboutLabel.setText(about);
    }

    public void setProfessorNameLabel(String professorName) {
        this.professorNameLabel.setText(professorName);
    }

    @FXML
    public Label professorNameLabel;

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    public void enterCourse(ActionEvent actionEvent) {
        FXMLLoader courseLoader=new FXMLLoader(getClass().getResource("../fxml/CourseTabPane.fxml"));
        Scene scene=null;
        try {
            scene=new Scene(courseLoader.load(), professorNameLabel.getScene().getWidth(), professorNameLabel.getScene().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) professorNameLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Course");
        CourseTabPaneController courseTabPaneController=courseLoader.getController();
        try {
            courseTabPaneController.first(course.getCourseId(),name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
