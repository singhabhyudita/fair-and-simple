package request;

import java.io.Serializable;

public class GetQuestionsRequest extends Request implements Serializable{
    private String examId;

    public GetQuestionsRequest(String examId) {
        this.examId = examId;
    }

    public String getExamId(){
        return examId;
    }
}