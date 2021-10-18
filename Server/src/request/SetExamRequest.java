package request;

import response.Question;

import java.util.Date;
import java.util.List;

public class SetExamRequest extends Request {
    private final String teacherId;
    private final String courseId;
    private final Date startTime;
    private final Date endTime;
    private final String examTitle;
    private final List<Question> questions;

    public SetExamRequest(String teacherId, String courseId, Date startTime, Date endTime, String examTitle, List<Question> questions) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.examTitle = examTitle;
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

    public List<Question> getQuestions() {
        return questions;
    }
}
