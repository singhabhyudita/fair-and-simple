package requestHandler;
import main.RequestIdentifier;
import request.SubmitExamRequest;
import table.ResultTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubmitExamRequestHandler extends RequestHandler {

    final private Connection connection;
    final private ObjectOutputStream oos;
    final private SubmitExamRequest request;

    public SubmitExamRequestHandler(Connection connection, ObjectOutputStream oos, SubmitExamRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        System.out.println("Exam id: "+request.getExam());
        try {
            PreparedStatement addResultMarks = connection.prepareStatement(ResultTable.ADD_RESULT_MARKS);
            addResultMarks.setString(1, userID);
            addResultMarks.setString(2,request.getExam().getExamId());
            addResultMarks.setInt(3,request.getNumberOfRightAnswers());
            addResultMarks.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
