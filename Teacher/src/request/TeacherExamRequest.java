package request;

public class TeacherExamRequest extends Request {
    private final String teacherId;
    private final boolean pastOnly;

    public TeacherExamRequest(String teacherId, boolean pastOnly) {
        this.teacherId = teacherId;
        this.pastOnly = pastOnly;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public boolean isPastOnly() {
        return pastOnly;
    }
}
