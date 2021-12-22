package requestHandler;

import main.Server;
import request.TeacherChangeProfilePictureRequest;
import response.TeacherChangeProfilePictureResponse;
import table.TeacherTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeacherChangeProfilePictureRequestHandler extends RequestHandler{
    Connection connection;
    ObjectOutputStream oos;
    TeacherChangeProfilePictureRequest request;
    public TeacherChangeProfilePictureRequestHandler(Connection connection, ObjectOutputStream oos, TeacherChangeProfilePictureRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        File image = request.getImage();
        try {
            FileInputStream fis = new FileInputStream(image);
            PreparedStatement statement = connection.prepareStatement(TeacherTable.CHANGE_PROFILE_PIC_QUERY);
            statement.setBinaryStream(1, fis, (int) image.length());
            TeacherChangeProfilePictureResponse response = new TeacherChangeProfilePictureResponse(statement.executeUpdate() == 1);
            Server.sendResponse(oos, response);
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        Server.sendResponse(oos, null);
    }
}
