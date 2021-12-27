package request;

public class CheckProctorJoinedRequest extends Request {
    private final String examID;

    public CheckProctorJoinedRequest(String examID) {
        this.examID = examID;
    }

    public String getExamID() {
        return examID;
    }
}
