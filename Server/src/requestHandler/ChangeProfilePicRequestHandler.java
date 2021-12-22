package requestHandler;

import main.RequestIdentifier;
import request.ChangeProfilePicRequest;
import response.ChangeProfilePicResponse;
import table.StudentTable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangeProfilePicRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    ChangeProfilePicRequest request;

    public ChangeProfilePicRequestHandler(Connection connection, ObjectOutputStream oos, ChangeProfilePicRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        ChangeProfilePicResponse changeProfilePicResponse = new ChangeProfilePicResponse("Failed");
        InputStream fis= new ByteArrayInputStream(request.getImage());
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(StudentTable.UPDATE_PROFILE_PIC_STUDENT);
            preparedStatement.setBlob(1, fis);
            preparedStatement.setString(2, userID);

            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows==1) changeProfilePicResponse.setResponse("Successful");
        }
        catch (SQLException e)
        {
            System.out.println(" Error occurred while profile pic :"+e.getMessage());
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
