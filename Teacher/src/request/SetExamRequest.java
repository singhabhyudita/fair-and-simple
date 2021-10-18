package request;

import entity.Question;

import java.util.Date;
import java.util.List;

public class SetExamRequest extends Request {
    private final String teacherId;
    private final String courseId;
    private final Date startTime;
    private final Date endTime;
    private final String examTitle;
    private final String description;
    private final List<Question> questions;

    public SetExamRequest(String teacherId, String courseId, Date startTime, Date endTime, String examTitle, String description, List<Question> questions) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.examTitle = examTitle;
        this.description = description;
        this.questions = questions;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public String getDescription() {
        return description;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
