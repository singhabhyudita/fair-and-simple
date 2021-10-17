package requestHandler;

import entity.Course;
import main.RequestIdentifier;
import response.CoursesListResponse;
import table.CoursesTable;
import table.EnrollmentTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CoursesListRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;

    public CoursesListRequestHandler(Connection connection, ObjectOutputStream oos) {
        this.connection = connection;
        this.oos = oos;
    }

    @Override
    public void sendResponse() {
       PreparedStatement p1;
        ArrayList<Course>courses = new ArrayList<>();
        ResultSet s1,s2;
        try {
            p1=connection.prepareStatement(EnrollmentTable.QUERY_GET_COURSES_BY_ID);
            p1.setInt(1,Integer.parseInt(RequestIdentifier.userID));
            s1=p1.executeQuery();
            while (s1.next()){
               PreparedStatement p2=connection.prepareStatement(CoursesTable.GET_COURSES_BY_COURSE_ID);
               p2.setInt(1,s1.getInt(1));
               s2=p2.executeQuery();
               while (s2.next()){
                   courses.add(new Course(s2.getString(CoursesTable.TEACHER_ID_COLUMN),s2.getString(CoursesTable.COURSE_ID_COLUMN),
                           s2.getString(CoursesTable.COURSE_NAME_COLUMN),s2.getString(CoursesTable.COURSE_CODE_COLUMN),
                           s2.getString(CoursesTable.COURSE_DESC_COLUMN)));
               }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(new CoursesListResponse(courses));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
