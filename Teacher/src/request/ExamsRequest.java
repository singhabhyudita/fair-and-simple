package request;

public class ExamsRequest extends Request {
    private final String teacherId;
    private final boolean pastOnly;

    public ExamsRequest(String teacherId, boolean pastOnly) {
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
