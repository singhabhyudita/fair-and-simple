package response;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable {
    private final String courseId;
    private final String teacherId;
    private final List<Student> students;
    private final String courseTitle;

    public Team(String courseId, String teacherId, List<Student> students, String courseTitle) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.students = students;
        this.courseTitle = courseTitle;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
}
