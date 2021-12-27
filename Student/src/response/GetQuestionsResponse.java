package response;

import entity.Question;
import java.io.Serializable;
import java.util.ArrayList;

public class GetQuestionsResponse extends Response implements Serializable{

    private ArrayList<Question> questionsList;
    private int proctorPort;
    private boolean alreadyAttempted;

    public GetQuestionsResponse(ArrayList<Question> questionsList, int proctorPort, boolean alreadyAttempted) {
        this.questionsList = questionsList;
        this.proctorPort = proctorPort;
        this.alreadyAttempted = alreadyAttempted;
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

    public boolean isAlreadyAttempted() {
        return alreadyAttempted;
    }

    public void setAlreadyAttempted(boolean alreadyAttempted) {
        this.alreadyAttempted = alreadyAttempted;
    }
}
