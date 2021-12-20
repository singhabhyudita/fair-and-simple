package request;

import entity.Question;
import response.Response;

import java.io.Serializable;
import java.util.ArrayList;

public class GetQuestionsResponse extends Response implements Serializable{

    private ArrayList<Question> questionsList;

    public GetQuestionsResponse(ArrayList<Question> questionsList) {
        this.questionsList = questionsList;
    }

    public ArrayList<Question> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(ArrayList<Question> questionsList) {
        this.questionsList = questionsList;
    }
}
