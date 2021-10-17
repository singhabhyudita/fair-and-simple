package response;

import entity.Exam;

import java.util.List;

public class TeacherExamResponse extends Response {
    private final List<Exam> exams;

    public TeacherExamResponse(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Exam> getExams() {
        return exams;
    }
}

