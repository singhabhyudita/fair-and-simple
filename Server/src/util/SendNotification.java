package util;

import entity.Message;
import entity.Notification;
import entity.RegistrationStreamWrapper;
import entity.TeacherIdStreamWrapper;
import main.Server;
import table.CoursesTable;
import table.EnrollmentTable;
import table.ExamTable;
import table.MessageTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SendNotification implements Runnable {
    Connection connection;
    @Override
    public void run() {
        connection=Server.getConnection();
        while (true) {
            try {
                Thread.sleep(59*1000);
                PreparedStatement preparedStatement=connection.prepareStatement(ExamTable.GET_EXAM_IN_NEXT_15_MINS);
                System.out.println("printing notif query");
                System.out.println(preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    sendToAll(new Notification(
                            null,
                            "Admin",
                            resultSet.getString(ExamTable.COURSE_ID_COLUMN),
                            resultSet.getString(CoursesTable.TABLE_NAME+"."+CoursesTable.COURSE_NAME_COLUMN),
                            resultSet.getString(ExamTable.TABLE_NAME+"."+ExamTable.TITLE_COLUMN)+" is about to start",
                            null,
                            Timestamp.valueOf(LocalDateTime.now()),
                            false),
                            resultSet.getString(ExamTable.TABLE_NAME+"."+ExamTable.PROCTOR_ID_COLUMN));
                    saveToDatabase(new Message(
                            null,
                    "Admin",
                            resultSet.getString(ExamTable.COURSE_ID_COLUMN),
                            resultSet.getString(CoursesTable.TABLE_NAME+"."+CoursesTable.COURSE_NAME_COLUMN),
                            resultSet.getString(ExamTable.TABLE_NAME+"."+ExamTable.TITLE_COLUMN)+" is about to start",
                            null,
                            Timestamp.valueOf(LocalDateTime.now()),
                            false));
                }
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToAll(Notification message,String proctorID){
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
        ArrayList<RegistrationStreamWrapper> socketArrayList= Server.socketArrayList;
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

        for (TeacherIdStreamWrapper r:Server.teacherSocketArrayList) {
            if(proctorID.equals(r.getTeacherId())){
                ObjectOutputStream objectOutputStream=r.getOos();
                System.out.println("sending proctor notif, got his oos"+objectOutputStream);
                try {
                    synchronized (objectOutputStream){
                        objectOutputStream.writeObject(message);
                        objectOutputStream.flush();
                        System.out.println("send notification to proctor");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void saveToDatabase(Message message){
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(MessageTable.ADD_MESSAGE_QUERY);
            preparedStatement.setNull(1,Types.INTEGER);
            preparedStatement.setString(2,message.getCourseID());
            preparedStatement.setString(3,message.getText());
            preparedStatement.setNull(4,Types.BLOB);
            preparedStatement.setTimestamp(5,message.getSentAt());
            preparedStatement.setBoolean(6,message.getStudent());
            int result=preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

