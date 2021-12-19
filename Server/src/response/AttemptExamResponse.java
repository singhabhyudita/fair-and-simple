package response;

import entity.Question;

import java.util.List;

public class AttemptExamResponse extends Response {
    final private List<Question> questions;
    final private String examId;

    public AttemptExamResponse(List<Question> questions, String examId) {
        this.questions = questions;
        this.examId = examId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getExamId() {
        return examId;
    }
}
