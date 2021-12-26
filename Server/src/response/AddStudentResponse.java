package response;

import entity.Status;

public class AddStudentResponse extends Response {
    private final Status status;

    public AddStudentResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
