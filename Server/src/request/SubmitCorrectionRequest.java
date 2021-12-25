package request;

import java.util.Map;

public class SubmitCorrectionRequest extends Request {

    private final String registrationNumber;
    private final String examId;
    private final Map<String, Integer> questionIdMarksMap;

    public SubmitCorrectionRequest(String registrationNumber, String examId, Map<String, Integer> questionIdMarksMap) {
        this.registrationNumber = registrationNumber;
        this.examId = examId;
        this.questionIdMarksMap = questionIdMarksMap;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getExamId() {
        return examId;
    }

    public Map<String, Integer> getQuestionIdMarksMap() {
        return questionIdMarksMap;
    }
}
