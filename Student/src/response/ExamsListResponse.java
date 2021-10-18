package Response;

import java.io.Serializable;
import java.util.ArrayList;
import Classes.*;

public class ExamsListResponse extends Response implements Serializable {
    private ArrayList<Exam> examsList;
    public ExamsListResponse(ArrayList<Exam> examsList) {
        this.examsList = examsList;
    }

    public ArrayList<Exam> getExamsList() {
        return examsList;
    }

    public void setExamsList(ArrayList<Exam> examsList) {
        this.examsList = examsList;
    }
}
