package entity;

import java.io.Serializable;

public class Course implements Serializable {
    private final String teacherId;
    private final String teacherName;
    private final String courseId;
    private final String courseName;
    private final String courseCode;
    private final String courseDescription;

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
}
