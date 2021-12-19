package request;

import entity.Question;

import java.util.List;

public class SubmitExamRequest extends Request {
    private final List<Question> questions;
    private final String examId;
    private final String courseId;

    public SubmitExamRequest(List<Question> questions, String examId, String courseId) {
        this.questions = questions;
        this.examId = examId;
        this.courseId = courseId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getExamId() {
        return examId;
    }

    public String getCourseId() {
        return courseId;
    }
}
