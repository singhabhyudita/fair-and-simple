package response;

public class TeacherChangePasswordResponse extends Response {
    private final int status;

    public TeacherChangePasswordResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
