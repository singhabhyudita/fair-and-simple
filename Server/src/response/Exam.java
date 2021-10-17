package response;

import java.io.Serializable;
import java.util.Date;

public class Exam implements Serializable {
    private final String examId;
    private final String teacherId;
    private final String courseId;
    private final String courseName;
    private final String title;
    private final Date date;
    private final Integer maximmumMarks;

    public Exam(String examId, String teacherId, String courseId, String courseName, String title, Date date, Integer maximmumMarks) {
        this.examId = examId;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.title = title;
        this.date = date;
        this.maximmumMarks = maximmumMarks;
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

    public String getCourseName() {
        return courseName;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Integer getMaximmumMarks() {
        return maximmumMarks;
    }
}
