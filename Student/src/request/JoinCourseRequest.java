package request;

import java.io.Serializable;

public class JoinCourseRequest extends Request implements Serializable {
    private String courseCode;

    public JoinCourseRequest(String courseCode){
        this.courseCode=courseCode;
    }

    public String getCourseCode(){
        return courseCode;
    }
}