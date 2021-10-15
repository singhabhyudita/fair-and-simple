package request;

public class TeacherCoursesRequest extends Request {
    private final String teacherId;

    public TeacherCoursesRequest(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }
}
