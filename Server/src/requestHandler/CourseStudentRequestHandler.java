package requestHandler;

import entity.Student;
import main.Server;
import request.CourseStudentRequest;
import response.CourseStudentResponse;
import table.StudentTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseStudentRequestHandler extends RequestHandler {

    Connection connection;
    ObjectOutputStream oos;
    CourseStudentRequest request;

    public CourseStudentRequestHandler(Connection connection, ObjectOutputStream oos, CourseStudentRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse() {
        String courseId = request.getCourseId();
        System.out.println("Requesting students with course id = " + courseId);
        PreparedStatement statement = null;
        List<Student> courseStudents = new ArrayList<>();
        try {
            statement = connection.prepareStatement(StudentTable.QUERY_STUDENT_BY_COURSE_ID);
            System.out.println("Course id sent in response is = " + courseId);
            statement.setString(1, courseId);
            System.out.println("The statement is = " + statement.toString());
            ResultSet students = statement.executeQuery();
            System.out.println("Got the following students -");
            while(students.next()) {
                courseStudents.add(new Student(
                        students.getString(StudentTable.COLUMN_FIRST_NAME) + " " + students.getString(StudentTable.COLUMN_LAST_NAME),
                        students.getString(StudentTable.COLUMN_REGISTRATION_NUMBER)));
                System.out.println(students.getString(StudentTable.COLUMN_REGISTRATION_NUMBER));
            }
            System.out.println("Done");
            CourseStudentResponse response = new CourseStudentResponse(courseStudents);
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null);
        }
    }
}
