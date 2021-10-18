package response;
import java.io.Serializable;

public class JoinCourseResponse extends Response implements Serializable {
    String response;
    String courseID;

    public JoinCourseResponse(String response, String courseID) {
        this.response = response;
        this.courseID = courseID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
