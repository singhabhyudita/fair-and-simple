package entity;

import javax.swing.*;
import java.sql.Timestamp;

public class Warning extends Notification {
    private final String receiverId;

    public Warning(String senderID, String senderName, String receiverId, String courseID, String courseName, String text, ImageIcon image, Timestamp sentAt, Boolean isStudent) {
        super(senderID, senderName, courseID, courseName, text, image, sentAt, isStudent);
        this.receiverId = receiverId;
    }

    public String getReceiverId() {
        return receiverId;
    }

}
