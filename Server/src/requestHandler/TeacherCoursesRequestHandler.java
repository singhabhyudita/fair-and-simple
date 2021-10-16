package requestHandler;

import main.Server;
import request.TeacherCoursesRequest;
import response.Course;
import response.TeacherCoursesResponse;
import table.CoursesTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherCoursesRequestHandler {
    private final Connection connection;
    private final ObjectOutputStream oos;
    private final TeacherCoursesRequest teacherCoursesRequest;
    public TeacherCoursesRequestHandler(Connection connection, ObjectOutputStream oos, TeacherCoursesRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.teacherCoursesRequest = request;
    }

    public void sendResposne() {
        ResultSet courseResultSet = null;
        try {
            PreparedStatement getCoursesStatement = connection.prepareStatement(CoursesTable.GET_COURSES_BY_TEACHER_ID);
            getCoursesStatement.setString(1, teacherCoursesRequest.getTeacherId());
            courseResultSet = getCoursesStatement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while(courseResultSet.next()) {
                courses.add(new Course(courseResultSet.getString(CoursesTable.TEACHER_ID_COLUMN),
                        courseResultSet.getString(CoursesTable.COURSE_ID_COLUMN),
                        courseResultSet.getString(CoursesTable.COURSE_NAME_COLUMN)));
            }
            TeacherCoursesResponse response = new TeacherCoursesResponse(teacherCoursesRequest.getTeacherId(), courses);
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null); // sending null response.
        }
    }
}