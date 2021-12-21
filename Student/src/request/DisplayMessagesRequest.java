package request;

public class DisplayMessagesRequest extends Request {
    private String courseID;

    public DisplayMessagesRequest(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }
}
