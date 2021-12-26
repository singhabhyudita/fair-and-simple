package request;

public class AddStudentRequest extends Request {
    private final String courseID;
    private final String registrationNo;

    public AddStudentRequest(String courseID, String registrationNo) {
        this.courseID = courseID;
        this.registrationNo = registrationNo;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }
}
