package response;

import entity.Question;

import java.util.List;
import java.util.Map;

public class GetResultForStudentResponse extends Response {
    private final List<Question> questions;
    private final List<Question> objectiveAnswers;
    private final byte [] subjectiveAnswer;
    private final Map<String, Integer> subjectiveQuestionMarks;
    private final boolean isCorrected;


    public GetResultForStudentResponse(List<Question> questions, List<Question> objectiveAnswers, byte[] subjectiveAnswer, Map<String, Integer> subjectiveQuestionMarks, boolean isCorrected) {
        this.questions = questions;
        this.objectiveAnswers = objectiveAnswers;
        this.subjectiveAnswer = subjectiveAnswer;
        this.subjectiveQuestionMarks = subjectiveQuestionMarks;
        this.isCorrected = isCorrected;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Question> getObjectiveAnswers() {
        return objectiveAnswers;
    }

    public byte[] getSubjectiveAnswer() {
        return subjectiveAnswer;
    }

    public Map<String, Integer> getSubjectiveQuestionMarks() {
        return subjectiveQuestionMarks;
    }

    public boolean isCorrected() {
        return isCorrected;
    }
}
