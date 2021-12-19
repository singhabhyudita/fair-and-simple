package requestHandler;

import entity.Question;
import main.Server;
import request.SubmitExamRequest;
import response.AttemptExamResponse;
import response.SubmitExamResponse;
import table.ExamQuestionsTable;
import table.ExamTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void sendResponse() {
        try {
            PreparedStatement getQuestions = connection.prepareStatement(ExamTable.GET_EXAM_BY_EXAM_ID);
            getQuestions.setString(1, request.getExamId());
            ResultSet questionsSet = getQuestions.executeQuery();
            HashMap<String, Integer> markedAnswer = new HashMap<>();
            HashMap<String, Boolean> corrected = new HashMap<>();
            for(Question q : request.getQuestions()) {
                markedAnswer.put(q.getQuestionID(), q.getCorrectOption()); // correct option means marked option in this context.
            }
            int marks = 0;
            while(questionsSet.next()) {
                String questionId = questionsSet.getString(ExamQuestionsTable.QUESTION_ID_COLUMN);
                Integer correctOption = questionsSet.getInt(ExamQuestionsTable.CORRECT_OPTION_COLUMN);
                corrected.put(questionId, markedAnswer.get(questionId).equals(correctOption));
                if(markedAnswer.get(questionId).equals(correctOption)) marks += 1;
            }
            SubmitExamResponse response = new SubmitExamResponse(corrected, marks, request.getExamId());
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null);
        }
    }
}
