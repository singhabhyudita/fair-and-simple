package request;

public class CourseStudentRequest extends Request {
    private final String courseId;

    public CourseStudentRequest(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
