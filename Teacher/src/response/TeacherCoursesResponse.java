package response;

import entity.Course;

import java.util.List;

public class TeacherCoursesResponse extends Response {
    private final String teacherId;
    private final List<Course> courses;

    public TeacherCoursesResponse(String teacherId, List<Course> courses) {
        this.teacherId = teacherId;
        this.courses = courses;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
