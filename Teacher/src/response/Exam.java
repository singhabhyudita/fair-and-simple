package response;

import java.io.Serializable;
import java.util.Date;

public class Exam implements Serializable {
    private final String examId;
    private final String teacherId;
    private final String courseId;
    private final String title;
    private final Date date;

    Exam(String examId, String teacherId, String courseId, String title, Date date) {
        this.examId = examId;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.title = title;
        this.date = date;
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

    public Date getDate() {
        return date;
    }
}
