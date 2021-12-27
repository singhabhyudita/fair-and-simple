package response;

import entity.Student;

import java.util.List;

public class ProctorPortForExamResponse extends Response {
    private final int proctorPort;
    private final List<Student> students;

    public ProctorPortForExamResponse(int proctorPort, List<Student> students) {
        this.proctorPort = proctorPort;
        this.students = students;
    }

    public int getProctorPort() {
        return proctorPort;
    }

    public List<Student> getStudents() {
        return students;
    }
}
