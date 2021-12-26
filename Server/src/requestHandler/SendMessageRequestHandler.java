package requestHandler;

import entity.Message;
import entity.RegistrationStreamWrapper;
import entity.TeacherIdStreamWrapper;
import main.Server;
import response.SendMessageResponse;
import sun.awt.image.ToolkitImage;
import table.CoursesTable;
import table.EnrollmentTable;
import table.MessageTable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SendMessageRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private Message message;

    public SendMessageRequestHandler(Connection connection, ObjectOutputStream oos, Message message) {
        this.connection = connection;
        this.oos = oos;
        this.message = message;
    }

    @Override
    public void sendResponse(String userID) {
        PreparedStatement preparedStatement;
        InputStream is=null;
        BufferedImage bufferedImage;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            preparedStatement=connection.prepareStatement(MessageTable.ADD_MESSAGE_QUERY);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2,message.getCourseID());
            preparedStatement.setString(3,message.getText());
            if(message.getImage() !=null) {
                bufferedImage = ((ToolkitImage) message.getImage().getImage()).getBufferedImage();
                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                is = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            }
            preparedStatement.setBlob(4,is);
            preparedStatement.setTimestamp(5,message.getSentAt());
            preparedStatement.setBoolean(6,message.getStudent());

            System.out.println("Message query");
            System.out.println(preparedStatement);
            int response=preparedStatement.executeUpdate();

            if(response==0)oos.writeObject(new SendMessageResponse("Failed"));
            else oos.writeObject(new SendMessageResponse("Successful"));

            oos.flush();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(){
        List<String> registrationNumbers = new ArrayList<>();
        try {
            PreparedStatement getRegistrationNumbers = connection.prepareStatement(EnrollmentTable.QUERY_GET_STUDENTS_BY_COURSE_ID);
            getRegistrationNumbers.setString(1, message.getCourseID());
            ResultSet enrolledStudents = getRegistrationNumbers.executeQuery();
            while(enrolledStudents.next()) {
                registrationNumbers.add(enrolledStudents.getString(EnrollmentTable.COLUMN_REGISTGRATION_NO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * Wrapper - oos registration no.
         * courseId -> registration no.
         */
        ArrayList<RegistrationStreamWrapper>socketArrayList= Server.socketArrayList;
        System.out.println("inside send to all");
        for (RegistrationStreamWrapper w:socketArrayList) {
            ObjectOutputStream oos = w.getOos();
            System.out.println("Chat oos  here:");
            System.out.println(oos.toString());
            try {
                //if(s.getOutputStream().equals(oos))continue;
                if(registrationNumbers.contains(w.getRegistrationNumber())) {
                    synchronized (oos){
                        oos.writeObject(message);
                        oos.flush();
                    }
                    System.out.println("message object sent");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Sending teacher
        String teacherId = null;
        try {
            PreparedStatement getRegistrationNumbers = connection.prepareStatement(CoursesTable.GET_TEACHER_ID_BY_COURSE_ID);
            getRegistrationNumbers.setString(1, message.getCourseID());
            ResultSet resultSet = getRegistrationNumbers.executeQuery();
            while(resultSet.next()) {
                teacherId = resultSet.getString(CoursesTable.TEACHER_ID_COLUMN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<TeacherIdStreamWrapper> teacherSocketArrayList= Server.teacherSocketArrayList;
        for (TeacherIdStreamWrapper t:teacherSocketArrayList) {
            ObjectOutputStream oos = t.getOos();
            try {
                System.out.println("##### TRYING TO SEND TEACHER ###" + t.getTeacherId());
                if(teacherId.equals(t.getTeacherId())) {
                    oos.writeObject(message);
                    oos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
