package entity;

import javax.swing.*;
import java.sql.Timestamp;

public class AddedInformation extends Notification {

    String receiverId;

    public AddedInformation(String senderID, String senderName, String receiverId, String courseID, String courseName, String text, ImageIcon image, Timestamp sentAt, Boolean isStudent) {
        super(senderID, senderName, courseID, courseName, text, image, sentAt, isStudent);
        this.receiverId = receiverId;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
