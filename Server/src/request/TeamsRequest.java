package request;

public class TeamsRequest extends Request {
    final private String teacherId;

    public TeamsRequest(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;
    }
}
