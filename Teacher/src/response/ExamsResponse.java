package response;

import java.util.List;

public class ExamsResponse extends Response {
    private final List<Exam> exams;

    public ExamsResponse(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Exam> getExams() {
        return exams;
    }
}

