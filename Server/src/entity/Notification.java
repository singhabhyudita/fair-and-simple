package entity;

import javax.swing.*;
import java.io.Serializable;
import java.sql.Timestamp;

public class Notification extends Message implements Serializable {
    public Notification(String senderID, String senderName, String courseID, String courseName, String text, ImageIcon image, Timestamp sentAt, Boolean isStudent) {
        super(senderID, senderName, courseID, courseName, text, image, sentAt, isStudent);
    }
}
