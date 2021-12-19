package response;

import java.io.File;

public class TeacherLoginResponse extends Response {
  private String firstName,lastName,emailID,teacherID;
  private File image;

    public TeacherLoginResponse(String firstName, String lastName, String emailID, String teacherID, File image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.teacherID = teacherID;
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
