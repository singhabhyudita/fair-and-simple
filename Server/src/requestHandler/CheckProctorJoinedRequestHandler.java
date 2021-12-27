package requestHandler;

import entity.Status;
import main.Server;
import request.CheckProctorJoinedRequest;
import response.CheckProctorJoinedResponse;
import table.ProctorPortTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckProctorJoinedRequestHandler extends RequestHandler{
    private Connection connection;
    private ObjectOutputStream oos;
    private CheckProctorJoinedRequest checkProctorJoinedRequest;

    public CheckProctorJoinedRequestHandler(Connection connection, ObjectOutputStream oos, CheckProctorJoinedRequest checkProctorJoinedRequest) {
        this.connection = connection;
        this.oos = oos;
        this.checkProctorJoinedRequest = checkProctorJoinedRequest;
    }

    @Override
    public void sendResponse(String userID) {
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(ProctorPortTable.GET_PORT_BY_EXAM_ID);
            preparedStatement.setString(1,checkProctorJoinedRequest.getExamID());
            ResultSet resultSet=preparedStatement.executeQuery();
            if(!resultSet.next()) Server.sendResponse(oos,new CheckProctorJoinedResponse(Status.PROCTOR_UNAVAILABLE));
            else Server.sendResponse(oos,new CheckProctorJoinedResponse(Status.PROCTOR_AVAILABLE));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
