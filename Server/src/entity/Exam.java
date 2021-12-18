package entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Exam implements Serializable {
    private final String examId;
    private final String teacherId;
    private final String courseId;
    private final String courseName;
    private final String title;
    private final String description;
    private final Timestamp date;
    private final Timestamp endTime;
    private final Integer maxMarks;


    public Exam(String examId, String teacherId, String courseId, String courseName, String title, String description, Timestamp date, Timestamp endTime, Integer maxMarks) {
        this.examId = examId;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.title = title;
        this.description = description;
        this.date = date;
        this.endTime = endTime;
        this.maxMarks = maxMarks;
    }

    public String getExamId() {
        return examId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getDate() {
        return date;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Integer getMaxMarks() {
        return maxMarks;
    }

    public String getCourseName() {
        return courseName;
    }
}
