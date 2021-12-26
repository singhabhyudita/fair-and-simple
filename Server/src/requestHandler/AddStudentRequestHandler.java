package requestHandler;

import entity.Status;
import main.Server;
import request.AddStudentRequest;
import response.AddStudentResponse;
import table.EnrollmentTable;
import table.StudentTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddStudentRequestHandler extends RequestHandler{
    private Connection connection;
    private ObjectOutputStream oos;
    private AddStudentRequest addStudentRequest;

    public AddStudentRequestHandler(Connection connection, ObjectOutputStream oos, AddStudentRequest addStudentRequest) {
        this.connection = connection;
        this.oos = oos;
        this.addStudentRequest = addStudentRequest;
    }

    @Override
    public void sendResponse(String userID) {
        try {
            PreparedStatement checkRegistrationNumber=connection.prepareStatement(StudentTable.QUERY_STUDENT_DETAILS_BY_ID);
            checkRegistrationNumber.setString(1,addStudentRequest.getRegistrationNo());
            ResultSet resultSet=checkRegistrationNumber.executeQuery();
            if(!resultSet.next()){
                Server.sendResponse(oos,new AddStudentResponse(Status.REGISTRATION_NUMBER_INVALID));
                return;
            }

            PreparedStatement preparedStatement=connection.prepareStatement(EnrollmentTable.QUERY_JOIN_COURSE_BY_ID);
            preparedStatement.setString(1,addStudentRequest.getCourseID());
            preparedStatement.setString(2,addStudentRequest.getRegistrationNo());
            int result=preparedStatement.executeUpdate();
            if (result==1)
                Server.sendResponse(oos,new AddStudentResponse(Status.STUDENT_ADDED));
            else
                Server.sendResponse(oos,new AddStudentResponse(Status.OTHER));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
