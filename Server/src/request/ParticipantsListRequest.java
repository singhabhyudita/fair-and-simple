package request;

import java.io.Serializable;

public class ParticipantsListRequest extends Request implements Serializable {
    private String courseId;

    public ParticipantsListRequest(String courseId){
        this.courseId=courseId;
    }

    public String getCourseId(){
        return courseId;
    }
}