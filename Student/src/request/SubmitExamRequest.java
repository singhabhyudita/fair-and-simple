package request;

import entity.Exam;

import java.io.Serializable;

public class SubmitExamRequest extends Request implements Serializable {
    private Exam exam;
    private Integer numberOfRightAnswers;

    public SubmitExamRequest(Exam exam, Integer numberOfRightAnswers) {
        this.exam = exam;
        this.numberOfRightAnswers = numberOfRightAnswers;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Integer getNumberOfRightAnswers() {
        return numberOfRightAnswers;
    }

    public void setNumberOfRightAnswers(Integer numberOfRightAnswers) {
        this.numberOfRightAnswers = numberOfRightAnswers;
    }
}
