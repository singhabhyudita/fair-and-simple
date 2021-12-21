package requestHandler;

import entity.Student;
import main.Server;
import request.ProctoringRequest;
import response.ProctoringResponse;
import table.ProctorPortTable;
import table.StudentTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProctoringRequestHandler extends RequestHandler {
    final private Connection connection;
    final private ObjectOutputStream oos;
    final private ProctoringRequest request;

    public ProctoringRequestHandler(Connection connection, ObjectOutputStream oos, ProctoringRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse() {
        try {
            PreparedStatement statement = connection.prepareStatement(ProctorPortTable.ADD_PROCTOR_PORT_FOR_EXAM);
            statement.setString(1, request.getExamId());
            statement.setInt(2, request.getListeningOnPort());
            int set = statement.executeUpdate();
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
            ProctoringResponse response = new ProctoringResponse(set == 1, students);
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
