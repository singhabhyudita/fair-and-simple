package request;

public class ExamsListRequest extends Request {
    private String courseId;

    /**
     * Request to display all the exams, both previous and upcoming, of a given course
     * @param courseId of the respective course
     */
    public ExamsListRequest(String courseId){
        this.courseId=courseId;
    }

    //Getter method
    public String getCourseId(){
        return courseId;
    }
}