package requestHandler;


import request.RegisterRequest;
import response.RegisterResponse;
import table.StudentTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterRequestHandler extends requestHandler.RequestHandler {
    private RegisterRequest registerRequest;
    private ObjectOutputStream oos;
    private Connection connection;

    public RegisterRequestHandler(RegisterRequest registerRequest, ObjectOutputStream oos, Connection connection) {
        this.registerRequest = registerRequest;
        this.oos = oos;
        this.connection = connection;
    }

    @Override
    public void sendResponse() {
        PreparedStatement preparedStatement;
        int result = 0;
        try {
            preparedStatement=connection.prepareStatement(StudentTable.QUERY_REGISTER);
            preparedStatement.setString(1,registerRequest.getUsername());
            preparedStatement.setString(2,registerRequest.getFirstName());
            preparedStatement.setString(3,registerRequest.getLastName());
            preparedStatement.setString(4,registerRequest.getEmailID());
            preparedStatement.setString(5,registerRequest.getPassword());
            result=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(result+" register query executed");
        if(result==0) {
            try {
                oos.writeObject(new RegisterResponse(""));
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                oos.writeObject(new RegisterResponse("Registered successfully"));
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
