package response;

import entity.Exam;

import java.util.List;

public class ProctoringDutyResponse extends Response {
    private final List<Exam> exams;

    public ProctoringDutyResponse(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Exam> getExams() {
        return exams;
    }
}
