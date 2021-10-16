package response;

public class TeacherRegisterResponse extends Response {
    private String message;

    public TeacherRegisterResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
