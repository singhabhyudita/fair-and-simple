package response;

import java.io.Serializable;

public class Course implements Serializable {
    private final String teacherId;
    private final String courseId;
    private final String courseTitle;

    public Course(String teacherId, String courseId, String courseTitle) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }
}
