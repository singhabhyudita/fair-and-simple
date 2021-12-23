package requestHandler;

import entity.Message;
import main.Server;
import request.DisplayMessagesRequest;
import response.DisplayMessagesResponse;
import table.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;

public class DisplayMessagesRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private DisplayMessagesRequest displayMessagesRequest;

    public DisplayMessagesRequestHandler(Connection connection, ObjectOutputStream oos, DisplayMessagesRequest displayMessagesRequest) {
        this.connection = connection;
        this.oos = oos;
        this.displayMessagesRequest = displayMessagesRequest;
    }

    @Override
    public void sendResponse(String userID) {
        ArrayList<Message> studentMessages = new ArrayList<>();
        ArrayList<Message> teacherMessages = new ArrayList<>();
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        DisplayMessagesResponse displayMessagesResponse;

        try {
            preparedStatement = connection.prepareStatement(MessageTable.GET_STUDENT_MESSAGES);
            preparedStatement.setString(1, displayMessagesRequest.getCourseID());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Blob blob=resultSet.getBlob(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_IMAGE);
                ByteArrayInputStream inputStream = null;
                ImageIcon imageIcon = null;
                if(blob != null) {
                    inputStream = (ByteArrayInputStream) blob.getBinaryStream();
                    BufferedImage bufferedImage = ImageIO.read(inputStream);
                    imageIcon = new ImageIcon(bufferedImage);
                }
                teacherMessages.add(new Message(
                        resultSet.getString(MessageTable.TABLE_NAME+"."+MessageTable.COLUMN_SENDER_ID),
                        resultSet.getString(StudentTable.TABLE_NAME+"."+ StudentTable.COLUMN_FIRST_NAME)
                        + " " + resultSet.getString(StudentTable.TABLE_NAME+"."+ StudentTable.COLUMN_LAST_NAME),
                        resultSet.getString(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_COURSE_ID),
                            resultSet.getString(CoursesTable.TABLE_NAME + "." + CoursesTable.COURSE_NAME_COLUMN),
                        resultSet.getString(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_TEXT),
                        imageIcon, // TODO :  IMAGE
                        resultSet.getTimestamp(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_SENT_AT),
                        true
                ));
            }

            preparedStatement = connection.prepareStatement(MessageTable.GET_TEACHER_MESSAGES);
            preparedStatement.setString(1, displayMessagesRequest.getCourseID());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Blob blob=resultSet.getBlob(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_IMAGE);
                ByteArrayInputStream inputStream = null;
                ImageIcon imageIcon = null;
                if(blob != null) {
                    inputStream = (ByteArrayInputStream) blob.getBinaryStream();
                    BufferedImage bufferedImage = ImageIO.read(inputStream);
                    imageIcon = new ImageIcon(bufferedImage);
                }
                studentMessages.add(new Message(
                        resultSet.getString(MessageTable.TABLE_NAME+"."+MessageTable.COLUMN_SENDER_ID),
                        resultSet.getString(TeacherTable.TABLE_NAME+"."+ TeacherTable.COLUMN_FIRST_NAME)
                                + " " + resultSet.getString(TeacherTable.TABLE_NAME+"."+ TeacherTable.COLUMN_LAST_NAME),
                        resultSet.getString(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_COURSE_ID),
                        resultSet.getString(MessageTable.TABLE_NAME + "." + CoursesTable.COURSE_NAME_COLUMN),
                        resultSet.getString(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_TEXT),
                        imageIcon,
                        resultSet.getTimestamp(MessageTable.TABLE_NAME + "." + MessageTable.COLUMN_SENT_AT),
                        false
                ));
            }
            displayMessagesResponse = new DisplayMessagesResponse(mergeFunction(studentMessages,teacherMessages));
            Server.sendResponse(oos,displayMessagesResponse);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Message> mergeFunction(ArrayList<Message> studentMessageList, ArrayList<Message> teacherMessageList) {
        ArrayList<Message> mergedMessagesList = new ArrayList<>();
        int i = 0, j = 0;
        while(i < studentMessageList.size() && j < teacherMessageList.size()) {
            if(studentMessageList.get(i).getSentAt().getTime() < teacherMessageList.get(j).getSentAt().getTime())
                mergedMessagesList.add(studentMessageList.get(i++));
            else
                mergedMessagesList.add(teacherMessageList.get(j++));
        }
        while(i < studentMessageList.size())
            mergedMessagesList.add(studentMessageList.get(i++));
        while(j < teacherMessageList.size())
            mergedMessagesList.add(teacherMessageList.get(j++));
        return mergedMessagesList;
    }
}