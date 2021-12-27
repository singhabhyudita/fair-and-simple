package request;

public class ProctorPortForExamRequest extends Request {

    private final String examId;

    public ProctorPortForExamRequest(String examId) {
        this.examId = examId;
    }

    public String getExamId() {
        return examId;
    }
}
