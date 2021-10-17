package request;

public class TeacherChangePasswordRequest extends Request {
    private final String teacherID;
    private final String oldPassword;
    private final String newPassword;

    public TeacherChangePasswordRequest(String teacherID, String oldPassword, String newPassword) {
        this.teacherID = teacherID;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
