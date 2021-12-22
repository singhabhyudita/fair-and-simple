package requestHandler;

import main.Server;
import request.TeacherChangePasswordRequest;
import response.TeacherChangePasswordResponse;
import table.TeacherTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherChangePasswordRequestHandler extends RequestHandler{
    private final Connection connection;
    private final ObjectOutputStream oos;
    private final TeacherChangePasswordRequest request;
    public TeacherChangePasswordRequestHandler(Connection connection, ObjectOutputStream oos, TeacherChangePasswordRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    public void sendResponse(String userID) {
        try {
            PreparedStatement getTeacher = connection.prepareStatement(TeacherTable.GET_TEACHER_BY_ID_PASSWORD);
            getTeacher.setString(1, request.getTeacherID());
            getTeacher.setString(2, request.getOldPassword());
            ResultSet teacher = getTeacher.executeQuery();
            if(teacher.next()) {
                PreparedStatement changePassword = connection.prepareStatement(TeacherTable.CHANGE_PASSWORD_QUERY);
                changePassword.setString(1, request.getNewPassword());
                changePassword.setString(2, request.getTeacherID());
                int result = changePassword.executeUpdate();
                if(result == 0) {
                    Server.sendResponse(oos, new TeacherChangePasswordResponse(-1));
                } else {
                    Server.sendResponse(oos, new TeacherChangePasswordResponse(0));
                }
            } else {
                Server.sendResponse(oos, new TeacherChangePasswordResponse(-1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, new TeacherChangePasswordResponse(-1));
        }
    }
}
