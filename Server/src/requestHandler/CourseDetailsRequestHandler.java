package requestHandler;

import entity.Course;
import request.CourseDetailsRequest;
import table.CoursesTable;
import table.TeacherTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseDetailsRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private CourseDetailsRequest courseDetailsRequest;

    public CourseDetailsRequestHandler(Connection connection, ObjectOutputStream oos, CourseDetailsRequest courseDetailsRequest) {
        this.connection = connection;
        this.oos = oos;
        this.courseDetailsRequest = courseDetailsRequest;
    }

    @Override
    public void sendResponse() {
        PreparedStatement preparedStatement,preparedStatement1;
        ResultSet resultSet,resultSet1;
        String teacherId,name;
        try {
            preparedStatement=connection.prepareStatement(CoursesTable.GET_COURSES_BY_COURSE_ID);
            preparedStatement.setString(1,courseDetailsRequest.getCourseID());
            resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                teacherId=resultSet.getString(CoursesTable.TEACHER_ID_COLUMN);

                preparedStatement1=connection.prepareStatement(TeacherTable.GET_TEACHER_NAME_BY_ID);
                preparedStatement1.setString(1,teacherId);
                resultSet1=preparedStatement1.executeQuery();
                resultSet1.next();

                name=resultSet1.getString(1)+" "+resultSet1.getString(2);
                oos.writeObject(new Course(teacherId,name,String.valueOf(resultSet.getInt(CoursesTable.COURSE_ID_COLUMN)),resultSet.getString(CoursesTable.COURSE_NAME_COLUMN),
                        resultSet.getString(CoursesTable.COURSE_CODE_COLUMN),resultSet.getString(CoursesTable.COURSE_DESC_COLUMN)));
                oos.flush();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
}
