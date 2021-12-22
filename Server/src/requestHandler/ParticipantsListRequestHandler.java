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
    public void sendResponse(String userID) {
        ArrayList<Student>students=new ArrayList<>();
        ResultSet resultSet;
        PreparedStatement preparedStatement= null;
        ParticipantsListResponse participantsListResponse;
        try {
            preparedStatement = connection.prepareStatement(EnrollmentTable.QUERY_GET_STUDENTS_BY_COURSE_ID);
            preparedStatement.setInt(1,Integer.parseInt(participantsListRequest.getCourseId()));
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                preparedStatement=connection.prepareStatement(StudentTable.QUERY_STUDENT_DETAILS_BY_ID);
                preparedStatement.setInt(1,resultSet.getInt(1));
                ResultSet resultSet1=preparedStatement.executeQuery();
                while (resultSet1.next()){
                    students.add(new Student(resultSet1.getInt(StudentTable.COLUMN_REGISTRATION_NUMBER),resultSet1.getString(StudentTable.COLUMN_FIRST_NAME)+" "+resultSet1.getString(StudentTable.COLUMN_LAST_NAME)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("sending students array "+ students.get(0).getName());
        try {
            participantsListResponse=new ParticipantsListResponse(students);
            oos.writeObject(participantsListResponse);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
