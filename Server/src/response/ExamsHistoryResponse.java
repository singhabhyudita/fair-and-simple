package response;


import entity.Exam;
import java.io.Serializable;
import java.util.ArrayList;

public class ExamsHistoryResponse extends Response implements Serializable {
    private ArrayList<Exam> examsList;
    public ExamsHistoryResponse(ArrayList<Exam> examsList) {
        this.examsList = examsList;
    }

    public ArrayList<Exam> getExamsList() {
        return examsList;
    }

    public void setExamsList(ArrayList<Exam> examsList) {
        this.examsList = examsList;
    }
}
