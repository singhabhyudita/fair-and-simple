package response;

import entity.Student;

import java.util.List;

public class ProctoringResponse extends Response {
    private final boolean setupDone;
    private final List<Student> students;

    public ProctoringResponse(boolean setupDone, List<Student> students) {
        this.setupDone = setupDone;
        this.students = students;
    }

    public boolean isSetupDone() {
        return setupDone;
    }

    public List<Student> getStudents() {
        return students;
    }
}
