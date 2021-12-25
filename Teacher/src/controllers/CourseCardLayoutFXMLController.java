package controllers;

import entity.Course;
import entity.Exam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;
import request.TeacherExamRequest;
import response.TeacherExamResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void setTeacherExamResponse(TeacherExamResponse teacherExamResponse) {
        this.teacherExamResponse = teacherExamResponse;
    }

    public TeacherExamResponse teacherExamResponse;

    public void enterCourse(ActionEvent actionEvent) {
        String courseTitle = course.getCourseName();
        String courseId = course.getCourseId();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CourseView.fxml"));
        Stage stage = (Stage) aboutLabel.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(loader.load(),aboutLabel.getScene().getWidth(),aboutLabel.getScene().getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle(courseTitle);

        CourseController controller = loader.getController();
        List<Exam> courseExam = getExamsForCourse(course.getCourseId());
        controller.callFirst(course.getCourseId(), course.getCourseName(),courseExam);
    }

    private List<Exam> getExamsForCourse(String courseId) {
        List<Exam> ret = new ArrayList<>();
        for(Exam e : teacherExamResponse.getExams())
            if(e.getCourseId().equals(courseId))
                ret.add(e);
        return ret;
    }

}
