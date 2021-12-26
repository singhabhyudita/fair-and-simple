package request;

public class AttemptExamRequest extends Request {
    final private String registrationNumber;
    final private String courseId;
    final private String examId;

    /**
     * When the client wants to attempt an exam, this request will be sent along with the following parameters
     * @param registrationNumber of the student who sent the request
     * @param courseId of the course in which the exam is being conducted
     * @param examId of the exam
     */
    public AttemptExamRequest(String registrationNumber, String courseId, String examId) {
        this.registrationNumber = registrationNumber;
        this.courseId = courseId;
        this.examId = examId;
    }

    //Getter and Setter methods:

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getExamId() {
        return examId;
    }
}
