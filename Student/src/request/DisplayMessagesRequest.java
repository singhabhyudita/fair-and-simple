package request;

public class DisplayMessagesRequest extends Request {
    private String courseID;

    /**
     * Request to display all the messages in the discussion forum of a particular course
     * @param courseID of the course for which the request is made
     */
    public DisplayMessagesRequest(String courseID) {
        this.courseID = courseID;
    }

    //Getter method
    public String getCourseID() {
        return courseID;
    }
}
