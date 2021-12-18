package request;

public class CourseDetailsRequest extends Request {
    private String courseID;

    public CourseDetailsRequest(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }
}
