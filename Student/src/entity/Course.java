package entity;

import java.io.Serializable;

public class Course implements Serializable {
    private String teacherId;
    private String teacherName;
    private String courseId;
    private String courseName;
    private String courseCode;
    private String courseDescription;

    public Course(String teacherId,String teacherName, String courseId, String courseName, String courseCode, String courseDescription) {
        this.teacherId=teacherId;
        this.teacherName = teacherName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseDescription = courseDescription;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public String getTeacherId() {
        return teacherId;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
