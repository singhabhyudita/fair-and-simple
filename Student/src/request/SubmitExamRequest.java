package request;

import entity.Exam;
import entity.Question;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class SubmitExamRequest extends Request implements Serializable {
    private Exam exam;
    private List<Question> objectiveAnswers;

    public SubmitExamRequest(Exam exam, List<Question> objectiveAnswers) {
        this.exam = exam;
        this.objectiveAnswers = objectiveAnswers;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<Question> getObjectiveAnswers() {
        return objectiveAnswers;
    }

    public void setObjectiveAnswers(List<Question> objectiveAnswers) {
        this.objectiveAnswers = objectiveAnswers;
    }
}
