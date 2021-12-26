package requestHandler;

import request.TeacherLoginRequest;
import response.TeacherLoginResponse;
import table.TeacherTable;
import java.io.*;
import java.sql.*;

public class TeacherLoginRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    TeacherLoginRequest request;

    public TeacherLoginRequestHandler(Connection connection, ObjectOutputStream oos, TeacherLoginRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID)  {
        PreparedStatement preparedStatement;
        TeacherLoginResponse response = null;
        try {
            preparedStatement=connection.prepareStatement(TeacherTable.QUERY_LOGIN);
            preparedStatement.setString(1,request.getUsername());
            preparedStatement.setString(2,request.getPassword());
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                response=new TeacherLoginResponse(resultSet.getString(TeacherTable.COLUMN_FIRST_NAME),resultSet.getString(TeacherTable.COLUMN_LAST_NAME),
                        resultSet.getString(TeacherTable.COLUMN_EMAIL_ID),resultSet.getString(TeacherTable.COLUMN_TEACHER_ID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
