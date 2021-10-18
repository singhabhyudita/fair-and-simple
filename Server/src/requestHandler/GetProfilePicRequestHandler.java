package requestHandler;

import Classes.Exam;
import request.UpcomingExamsRequest;
import response.ExamsHistoryResponse;
import table.ExamTable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import request.*;
import response.*;
import Classes.*;
import table.StudentTable;

public class GetProfilePicRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    GetProfilePicRequest request;
    String registrationNumber;

    public GetProfilePicRequestHandler(Connection connection, ObjectOutputStream oos, GetProfilePicRequest request,String registrationNumber) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public void sendResponse() {
        GetProfilePicResponse getProfilePicResponse = null;
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(StudentTable.SELECT_PROFILE_PIC_STUDENT);
            preparedStatement.setString(1,registrationNumber);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next())
                getProfilePicResponse = new GetProfilePicResponse(results.getBinaryStream(1));
        }
        catch (SQLException e)
        {
            System.out.println(" Error occurred while sending exams history list"+e.getMessage());
        }
        finally
        {
            try
            {
                oos.writeObject(getProfilePicResponse);
                oos.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
