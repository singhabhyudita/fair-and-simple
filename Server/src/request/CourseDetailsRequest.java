package request;

public class CourseDetailsRequest extends Request {
    private String courseID;

    /**
     * When a student requests the course details, this request is sent
     * @param courseID of the course
     */
    public CourseDetailsRequest(String courseID) {
        this.courseID = courseID;
    }

    //Getter method
    public String getCourseID() {
        return courseID;
    }
}
