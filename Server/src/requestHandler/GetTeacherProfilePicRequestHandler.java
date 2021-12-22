package requestHandler;

import main.RequestIdentifier;
import request.GetTeacherProfilePicRequest;
import response.GetTeacherProfilePicResponse;
import table.TeacherTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;

public class GetTeacherProfilePicRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private GetTeacherProfilePicRequest getTeacherProfilePicRequest;

    public GetTeacherProfilePicRequestHandler(Connection connection, ObjectOutputStream oos, GetTeacherProfilePicRequest getTeacherProfilePicRequest) {
        this.connection = connection;
        this.oos = oos;
        this.getTeacherProfilePicRequest = getTeacherProfilePicRequest;
    }

    @Override
    public void sendResponse(String userID) {
        GetTeacherProfilePicResponse getTeacherProfilePicResponse = null;
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(TeacherTable.GET_PROFILE_PIC_BY_TEACHER_ID);
            preparedStatement.setString(1, userID);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()){
                Blob blob=results.getBlob(1);
                if(blob != null) {
                    ByteArrayInputStream inputStream= (ByteArrayInputStream) blob.getBinaryStream();
                    BufferedImage bufferedImage = ImageIO.read(inputStream);
                    System.out.println("Buffered image is "+bufferedImage);
                    ImageIcon imageIcon=new ImageIcon(bufferedImage);
                    getTeacherProfilePicResponse = new GetTeacherProfilePicResponse(imageIcon);
                } else {
                    getTeacherProfilePicResponse = new GetTeacherProfilePicResponse(null);
                }
            }
        }
        catch (SQLException | IOException e)
        {
            System.out.println(" Error occurred while getting profile pic "+e.getMessage());
        }
        finally
        {
            try
            {
                oos.writeObject(getTeacherProfilePicResponse);
                oos.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
