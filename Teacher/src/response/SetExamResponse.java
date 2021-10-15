package response;

public class SetExamResponse extends Response {
    private final Status status;

    public SetExamResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
