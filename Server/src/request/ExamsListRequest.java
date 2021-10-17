package request;

import java.io.Serializable;

public class ExamsListRequest extends Request implements Serializable {
    private String courseId;

    public ExamsListRequest(String courseId){
        this.courseId=courseId;
    }

    public String getCourseId(){
        return courseId;
    }
}