package requestHandler;

import request.TeacherRegisterRequest;
import response.TeacherRegisterResponse;
import table.TeacherTable;

import java.io.*;
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
    public void sendResponse(String userID) {
        PreparedStatement preparedStatement;
        File file=new File("src/images/sample.png");
        FileInputStream fis;
        int result=0;
        try {
            preparedStatement=connection.prepareStatement(TeacherTable.QUERY_REGISTER);
            preparedStatement.setString(1,registerRequest.getTeacherID());
            preparedStatement.setString(2,registerRequest.getFirstName());
            preparedStatement.setString(3,registerRequest.getLastName());
            preparedStatement.setString(4,registerRequest.getEmailID());
            preparedStatement.setString(5,registerRequest.getPassword());
            fis=new FileInputStream(file);
            preparedStatement.setBinaryStream(6,fis);
            System.out.println("Teacher register query:");
            System.out.println(preparedStatement);
            result=preparedStatement.executeUpdate();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }

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
