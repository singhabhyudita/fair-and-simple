package requestHandler;

import entity.Student;
import request.ParticipantsListRequest;
import response.ParticipantsListResponse;
import table.EnrollmentTable;
import table.StudentTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ParticipantsListRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private ParticipantsListRequest participantsListRequest;

    public ParticipantsListRequestHandler(Connection connection, ObjectOutputStream oos, ParticipantsListRequest participantsListRequest) {
        this.connection = connection;
        this.oos = oos;
        this.participantsListRequest = participantsListRequest;
    }

    @Override
    public void sendResponse() {
        ArrayList<Student>students=new ArrayList<>();
        ResultSet resultSet;
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = connection.prepareStatement(EnrollmentTable.QUERY_GET_STUDENTS_BY_COURSE_ID);
            preparedStatement.setInt(1,Integer.parseInt(participantsListRequest.getCourseId()));
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                preparedStatement=connection.prepareStatement(StudentTable.QUERY_STUDENT_DETAILS_BY_ID);
                preparedStatement.setInt(1,resultSet.getInt(1));
                ResultSet resultSet1=preparedStatement.executeQuery();
                while (resultSet1.next()){
                    students.add(new Student(resultSet1.getString(StudentTable.COLUMN_FIRST_NAME)+resultSet1.getString(StudentTable.COLUMN_LAST_NAME),
                            resultSet1.getString(StudentTable.COLUMN_REGISTRATION_NUMBER)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(new ParticipantsListResponse(students));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
