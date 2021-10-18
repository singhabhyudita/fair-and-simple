package Response;

import java.io.Serializable;
import java.util.ArrayList;
import Classes.*;

public class CoursesListResponse extends Response implements Serializable {
    private ArrayList<Course> coursesList;
    public CoursesListResponse(ArrayList<Course> CoursesList) {
        this.coursesList = CoursesList;
    }

    public ArrayList<Course> getCoursesList() {
        return coursesList;
    }

    public void setCoursesList(ArrayList<Course> coursesList) {
        this.coursesList = coursesList;
    }
}
