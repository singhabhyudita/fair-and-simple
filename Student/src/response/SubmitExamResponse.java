package response;

public class SubmitExamResponse extends Response {
    private final boolean sendFileStream;

    public SubmitExamResponse(boolean sendFileStream) {
        this.sendFileStream = sendFileStream;
    }

    public boolean isSendFileStream() {
        return sendFileStream;
    }
}
