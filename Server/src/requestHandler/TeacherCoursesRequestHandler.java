package requestHandler;

import main.Server;
import request.TeacherCoursesRequest;
import entity.Course;
import response.TeacherCoursesResponse;
import table.CoursesTable;
import table.TeacherTable;

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
        ResultSet courseResultSet = null,resultSet;
        PreparedStatement preparedStatement;
        String teacherId,name = null;
        try {
            PreparedStatement getCoursesStatement = connection.prepareStatement(CoursesTable.GET_COURSES_BY_TEACHER_ID);
            getCoursesStatement.setString(1, teacherCoursesRequest.getTeacherId());
            System.out.println("Query used to get sources = " + getCoursesStatement.toString());
            courseResultSet = getCoursesStatement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while(courseResultSet.next()) {
                teacherId=courseResultSet.getString(CoursesTable.TEACHER_ID_COLUMN);

                preparedStatement=connection.prepareStatement(TeacherTable.GET_TEACHER_NAME_BY_ID);
                preparedStatement.setString(1,teacherId);
                resultSet=preparedStatement.executeQuery();
                resultSet.next();

                name=resultSet.getString(1);

                courses.add(new Course(teacherId,name,
                        courseResultSet.getString(CoursesTable.COURSE_ID_COLUMN),
                        courseResultSet.getString(CoursesTable.COURSE_NAME_COLUMN), courseResultSet.getString(CoursesTable.COURSE_CODE_COLUMN),
                        courseResultSet.getString(CoursesTable.COURSE_DESC_COLUMN)));
                System.out.println("Getting course = " + courseResultSet.getString(CoursesTable.COURSE_NAME_COLUMN));
            }
            TeacherCoursesResponse response = new TeacherCoursesResponse(teacherCoursesRequest.getTeacherId(), courses);
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null); // sending null response.
        }
    }
}