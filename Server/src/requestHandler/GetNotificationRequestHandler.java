package requestHandler;

import entity.Notification;
import request.GetNotificationRequest;
import response.GetNotificationResponse;
import table.CoursesTable;
import table.MessageTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetNotificationRequestHandler extends RequestHandler{
    private Connection connection;
    private ObjectOutputStream oos;
    private GetNotificationRequest getNotificationRequest;

    public GetNotificationRequestHandler(Connection connection, ObjectOutputStream oos, GetNotificationRequest getNotificationRequest) {
        this.connection = connection;
        this.oos = oos;
        this.getNotificationRequest = getNotificationRequest;
    }

    @Override
    public void sendResponse(String userID) {
        ArrayList<Notification>notificationArrayList=new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(MessageTable.GET_NOTIFICATION);
            preparedStatement.setString(1,userID);
            System.out.println(preparedStatement);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                notificationArrayList.add(
                        new Notification
                                (null,
                                        "Admin",
                                        resultSet.getString(MessageTable.TABLE_NAME+"."+MessageTable.COLUMN_COURSE_ID),
                                        resultSet.getString(CoursesTable.TABLE_NAME+"."+CoursesTable.COURSE_NAME_COLUMN),
                                        resultSet.getString(MessageTable.TABLE_NAME+"."+MessageTable.COLUMN_TEXT),
                                        null,
                                        resultSet.getTimestamp(MessageTable.TABLE_NAME+"."+MessageTable.COLUMN_SENT_AT),
                                        resultSet.getBoolean(MessageTable.TABLE_NAME+"."+MessageTable.COLUMN_IS_STUDENT)
                                )
                );
            }
            oos.writeObject(new GetNotificationResponse(notificationArrayList));
            oos.flush();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
