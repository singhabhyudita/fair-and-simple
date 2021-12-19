package response;

import java.util.HashMap;

public class SubmitExamResponse extends Response {
    private final HashMap<String, Boolean> correct;
    private final int marksObtained;
    private final String examId;

    public SubmitExamResponse(HashMap<String, Boolean> correct, int marksObtained, String examId) {
        this.correct = correct;
        this.marksObtained = marksObtained;
        this.examId = examId;
    }

    public HashMap<String, Boolean> getCorrect() {
        return correct;
    }

    public int getMarksObtained() {
        return marksObtained;
    }

    public String getExamId() {
        return examId;
    }
}
