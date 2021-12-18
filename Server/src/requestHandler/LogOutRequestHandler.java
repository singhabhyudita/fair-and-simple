package requestHandler;

import main.RequestIdentifier;
import request.LogOutRequest;
import request.Request;
import response.LogOutResponse;
import table.StudentTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogOutRequestHandler extends RequestHandler  {
    private Connection connection;
    private ObjectOutputStream oos;

    public LogOutRequestHandler(Connection connection, ObjectOutputStream oos) {
        this.connection = connection;
        this.oos = oos;
    }

    @Override
    public void sendResponse() {
        int result=0;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(StudentTable.QUERY_UPDATE_LAST_ACTIVE);
            System.out.println("User Id = " + RequestIdentifier.userID);
            preparedStatement.setInt(1,Integer.parseInt(RequestIdentifier.userID));
            result=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
          if(result==0)oos.writeObject(new LogOutResponse(""));
          else oos.writeObject(new LogOutResponse("Successful"));
          oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
