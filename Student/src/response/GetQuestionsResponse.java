package response;

import entity.Question;
import java.io.Serializable;
import java.util.ArrayList;

public class GetQuestionsResponse extends Response implements Serializable{

    private ArrayList<Question> questionsList;
    private int proctorPort;

    public GetQuestionsResponse(ArrayList<Question> questionsList, int proctorPort) {
        this.questionsList = questionsList;
        this.proctorPort = proctorPort;
    }

    public ArrayList<Question> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(ArrayList<Question> questionsList) {
        this.questionsList = questionsList;
    }

    public int getProctorPort() {
        return proctorPort;
    }

    public void setProctorPort(int proctorPort) {
        this.proctorPort = proctorPort;
    }
}
