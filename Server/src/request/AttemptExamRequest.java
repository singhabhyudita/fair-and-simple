package request;

public class AttemptExamRequest extends Request {
    final private String registrationNumber;
    final private String courseId;
    final private String examId;

    public AttemptExamRequest(String registrationNumber, String courseId, String examId) {
        this.registrationNumber = registrationNumber;
        this.courseId = courseId;
        this.examId = examId;
    }

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
