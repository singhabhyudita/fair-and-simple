package requestHandler;

import entity.Student;
import main.Server;
import request.ProctorPortForExamRequest;
import response.ProctorPortForExamResponse;
import response.Response;
import table.ProctorPortTable;
import table.StudentTable;

import javax.xml.transform.Result;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProctorPortForExamRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private ProctorPortForExamRequest request;

    public ProctorPortForExamRequestHandler(Connection connection, ObjectOutputStream oos, ProctorPortForExamRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        try {
            PreparedStatement statement = connection.prepareStatement(ProctorPortTable.GET_PORT_BY_EXAM_ID);
            statement.setString(1, request.getExamId());
            ResultSet resultSet = statement.executeQuery();
            statement = connection.prepareStatement(StudentTable.QUERY_STUDENT_BY_EXAM_ID);
            statement.setString(1, request.getExamId());
            ResultSet studentsSet = statement.executeQuery();
            List<Student> students = new ArrayList<>();
            while(studentsSet.next()) {
                students.add(new Student(
                        studentsSet.getInt(StudentTable.COLUMN_REGISTRATION_NUMBER),
                        studentsSet.getString(StudentTable.COLUMN_FIRST_NAME)
                                + " " + studentsSet.getString(StudentTable.COLUMN_LAST_NAME)
                ));
            }
            ProctorPortForExamResponse response = null;
            if(resultSet.next()) {
                response = new ProctorPortForExamResponse(resultSet.getInt(ProctorPortTable.COLUMN_PROCTOR_PORT), students);
            }
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
