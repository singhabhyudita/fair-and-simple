package entity;

import java.io.Serializable;

public class Course implements Serializable {
    private final String teacherId;
    private final String courseId;
    private final String courseName;
    private final String courseCode;
    private final String courseDescription;

    public Course(String teacherId, String courseId, String courseName, String courseCode, String courseDescription) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseDescription = courseDescription;
    }
    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }
}
