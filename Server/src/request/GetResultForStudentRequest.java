package request;

public class GetResultForStudentRequest extends Request {

    private final String studentId;
    private final String examId;
    private final String courseId;

    public GetResultForStudentRequest(String studentId, String examId, String courseId) {
        this.studentId = studentId;
        this.examId = examId;
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getExamId() {
        return examId;
    }

    public String getCourseId() {
        return courseId;
    }
}
