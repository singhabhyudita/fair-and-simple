package response;

import entity.Status;

public class CheckProctorJoinedResponse extends Response{
    private final Status status;

    public CheckProctorJoinedResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
