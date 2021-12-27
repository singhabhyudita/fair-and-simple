package response;

public class CreateCourseResponse extends Response {
    private final String courseCode;
    private final String courseID;

    public CreateCourseResponse(String courseCode, String courseID) {
        this.courseCode = courseCode;
        this.courseID = courseID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseID() {
        return courseID;
    }
}
