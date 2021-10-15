package request;

public class ExamResultRequest extends Request {
    private final String examId;

    public ExamResultRequest(String examId) {
        this.examId = examId;
    }

    public String getExamId() {
        return examId;
    }
}
