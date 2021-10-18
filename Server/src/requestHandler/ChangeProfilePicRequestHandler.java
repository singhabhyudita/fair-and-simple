package requestHandler;

import request.GetProfilePicRequest;
import response.GetProfilePicResponse;
import table.StudentTable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import request.*;
import response.*;
import Classes.*;
import table.StudentTable;

public class ChangeProfilePicRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    ChangeProfilePicRequest request;
    FileInputStream fis;
    String registrationNumber;

    public ChangeProfilePicRequestHandler(Connection connection, ObjectOutputStream oos, ChangeProfilePicRequest request,FileInputStream fis,String registrationNumber) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
        this.fis = fis;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public void sendResponse() {
        ChangeProfilePicResponse changeProfilePicResponse = new ChangeProfilePicResponse("Failed");
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(StudentTable.UPDATE_PROFILE_PIC_STUDENT);
            preparedStatement.setBinaryStream(1, fis, fis.available());
            preparedStatement.setString(2,registrationNumber);

            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows==1)
                changeProfilePicResponse.setResponse("Successful");
        }
        catch (SQLException | IOException e)
        {
            System.out.println(" Error occurred while sending exams history list"+e.getMessage());
        }
        finally
        {
            try
            {
                oos.writeObject(changeProfilePicResponse);
                oos.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
