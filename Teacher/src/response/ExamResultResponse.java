package response;

import java.util.List;

public class ExamResultResponse extends Response {
    private final String examId;
    private final List<StudentResult> results;

    public ExamResultResponse(String examId, List<StudentResult> results) {
        this.examId = examId;
        this.results = results;
    }

    public String getExamId() {
        return examId;
    }

    public List<StudentResult> getResults() {
        return results;
    }
}

