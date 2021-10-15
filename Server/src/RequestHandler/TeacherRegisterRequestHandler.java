package RequestHandler;

import Request.TeacherRegisterRequest;
import Response.TeacherRegisterResponse;
import Table.TeacherTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeacherRegisterRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    TeacherRegisterRequest registerRequest;

    public TeacherRegisterRequestHandler(Connection connection, ObjectOutputStream oos, TeacherRegisterRequest registerRequest) {
        this.connection = connection;
        this.oos = oos;
        this.registerRequest = registerRequest;
    }

    @Override
    public void sendResponse() throws SQLException {
        PreparedStatement preparedStatement=null;
        preparedStatement=connection.prepareStatement(TeacherTable.QUERY_REGISTER);
        preparedStatement.setString(1,registerRequest.getTeacherID());
        preparedStatement.setString(2,registerRequest.getFirstName());
        preparedStatement.setString(3,registerRequest.getLastName());
        preparedStatement.setString(4,registerRequest.getEmailID());
        preparedStatement.setString(5,registerRequest.getPassword());

        int result=preparedStatement.executeUpdate();
        System.out.println(result+" register query executed");
        if(result==0) {
            try {
                oos.writeObject(new TeacherRegisterResponse(""));
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                oos.writeObject(new TeacherRegisterResponse("Registered successfully"));
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
