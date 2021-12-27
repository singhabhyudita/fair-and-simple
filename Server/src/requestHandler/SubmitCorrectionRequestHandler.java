package requestHandler;

import request.SubmitCorrectionRequest;
import table.ExamQuestionsMarksTable;
import table.ExamQuestionsTable;
import table.ObjectiveResponseTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SubmitCorrectionRequestHandler extends RequestHandler {
    private final Connection connection;
    private ObjectOutputStream oos;
    private final SubmitCorrectionRequest request;
    public SubmitCorrectionRequestHandler(Connection connection, ObjectOutputStream oos, SubmitCorrectionRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        try {
            PreparedStatement statement = connection.prepareStatement(ExamQuestionsMarksTable.ADD_ENTRY_QUERY);
            System.out.println("Registration No = " + request.getRegistrationNumber());
            statement.setString(1, request.getRegistrationNumber());
            statement.setString(2, request.getExamId());
            for(String questionId : request.getQuestionIdMarksMap().keySet()) {
                statement.setString(3, questionId);
                statement.setInt(4, request.getQuestionIdMarksMap().get(questionId));
                statement.executeUpdate();
            }
            statement = connection.prepareStatement(ExamQuestionsTable.GET_OBJECTIVE_QUESTIONS_BY_EXAM_ID);
            statement.setString(1, request.getExamId());
            Map<String, Integer> correctObjectiveAnswer = new HashMap<>();
            Map<String, Integer> markedObjectiveAnswer = new HashMap<>();
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                correctObjectiveAnswer.put(
                        set.getString(ExamQuestionsTable.QUESTION_ID_COLUMN),
                        set.getInt(ExamQuestionsTable.CORRECT_OPTION_COLUMN));
            }
            statement = connection.prepareStatement(ObjectiveResponseTable.GET_ENTRY_BY_EXAM_ID_AND_REGISTRATION_NO);
            statement.setString(1, request.getExamId());
            statement.setString(2, request.getRegistrationNumber());
            set = statement.executeQuery();
            while(set.next()) {
                markedObjectiveAnswer.put(
                        set.getString(ObjectiveResponseTable.COLUMN_QUESTION_ID),
                        set.getInt(ObjectiveResponseTable.COLUMN_MARKED));
            }
            statement = connection.prepareStatement(ExamQuestionsMarksTable.ADD_ENTRY_QUERY);
            statement.setString(1, request.getRegistrationNumber());
            statement.setString(2, request.getExamId());
            for(String questionId : correctObjectiveAnswer.keySet()) {
                statement.setString(3, questionId);
                statement.setInt(4, Objects.equals(correctObjectiveAnswer.get(questionId), markedObjectiveAnswer.get(questionId)) ? 1 : 0);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
