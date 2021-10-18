package response;

class StudentResult {
    private final String studentId;
    private final String examId;
    private final String registrationNumber;
    private final String name;
    private final int marks;

    StudentResult(String studentId, String examId, String registrationNumber, String name, int marks) {
        this.studentId = studentId;
        this.examId = examId;
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.marks = marks;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getExamId() {
        return examId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }
}
