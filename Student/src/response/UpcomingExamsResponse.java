package Response;

import Classes.*;
import java.io.Serializable;
import java.util.ArrayList;

public class UpcomingExamsResponse extends Response implements Serializable {
    private ArrayList<Exam> examsList;
    UpcomingExamsResponse(ArrayList<Exam> examsList) {
        this.examsList = examsList;
    }

    public ArrayList<Exam> getExamsList() {
        return examsList;
    }

    public void setExamsList(ArrayList<Exam> examsList) {
        this.examsList = examsList;
    }
}
