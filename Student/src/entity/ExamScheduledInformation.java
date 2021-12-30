package entity;

import javax.swing.*;
import java.sql.Timestamp;

public class ExamScheduledInformation extends Notification {
    public ExamScheduledInformation(String senderID, String senderName, String courseID, String courseName, String text, ImageIcon image, Timestamp sentAt, Boolean isStudent) {
        super(senderID, senderName, courseID, courseName, text, image, sentAt, isStudent);
    }
}
