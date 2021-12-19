package requestHandler;

import main.RequestIdentifier;
import request.ChangeTeacherProfilePicRequest;
import response.ChangeTeacherProfilePicResponse;
import table.TeacherTable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangeTeacherProfilePicRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private ChangeTeacherProfilePicRequest changeTeacherProfilePicRequest;

    public ChangeTeacherProfilePicRequestHandler(Connection connection, ObjectOutputStream oos, ChangeTeacherProfilePicRequest changeTeacherProfilePicRequest) {
        this.connection = connection;
        this.oos = oos;
        this.changeTeacherProfilePicRequest = changeTeacherProfilePicRequest;
    }

    @Override
    public void sendResponse() {
        ChangeTeacherProfilePicResponse changeTeacherProfilePicResponse = new ChangeTeacherProfilePicResponse("Failed");
        InputStream fis= new ByteArrayInputStream(changeTeacherProfilePicRequest.getImage());
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(TeacherTable.CHANGE_PROFILE_PIC_QUERY);
            preparedStatement.setBlob(1, fis);
            preparedStatement.setString(2, RequestIdentifier.userID);
            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows==1) changeTeacherProfilePicResponse.setResponse("Successful");
        }
        catch (SQLException e)
        {
            System.out.println(" Error occurred while profile pic :"+e.getMessage());
        }
        finally
        {
            try
            {
                oos.writeObject(changeTeacherProfilePicResponse);
                oos.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
