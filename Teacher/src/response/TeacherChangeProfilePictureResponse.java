package response;

public class TeacherChangeProfilePictureResponse extends Response {
    private final boolean success;

    public TeacherChangeProfilePictureResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
