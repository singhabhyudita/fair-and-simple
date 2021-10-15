package RequestHandler;

import Request.TeacherLoginRequest;
import Response.TeacherLoginResponse;
import Table.TeacherTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void sendResponse() throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement(TeacherTable.QUERY_LOGIN);
        preparedStatement.setString(1,request.getUsername());
        preparedStatement.setString(2,request.getPassword());
        ResultSet resultSet=preparedStatement.executeQuery();
        TeacherLoginResponse response;
        if(!resultSet.next()) response=null;
        do{
            response=new TeacherLoginResponse(resultSet.getString(TeacherTable.COLUMN_FIRST_NAME),resultSet.getString(TeacherTable.COLUMN_LAST_NAME),
                    resultSet.getString(TeacherTable.COLUMN_EMAIL_ID),resultSet.getString(TeacherTable.COLUMN_TEACHER_ID));
        }while (resultSet.next());
        try {
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
