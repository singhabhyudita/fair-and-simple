package response;

import entity.Student;

import java.util.List;

public class CourseStudentResponse extends Response {
    private final List<Student> students;

    public CourseStudentResponse(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }
}
