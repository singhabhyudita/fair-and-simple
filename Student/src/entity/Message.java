package entity;

import request.Request;

import javax.swing.*;
import java.io.Serializable;
import java.sql.Timestamp;

public class Message extends Request implements Serializable {
  //  private String messageID;
    private String senderID;
    private String senderName;
    private String courseID;
    private String courseName;
    private String text;
    private ImageIcon image;
    private Timestamp sentAt;
    private Boolean isStudent;

    public Message(String senderID, String senderName, String courseID, String courseName, String text, ImageIcon image, Timestamp sentAt, Boolean isStudent) {
        this.senderID = senderID;
        this.senderName = senderName;
        this.courseID = courseID;
        this.courseName = courseName;
        this.text = text;
        this.image = image;
        this.sentAt = sentAt;
        this.isStudent = isStudent;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getStudent() {
        return isStudent;
    }

    public void setStudent(Boolean student) {
        isStudent = student;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
