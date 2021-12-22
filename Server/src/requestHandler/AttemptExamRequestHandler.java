package requestHandler;

import entity.Question;
import main.Server;
import request.AttemptExamRequest;
import response.AttemptExamResponse;
import table.ExamQuestionsTable;
import table.ExamTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttemptExamRequestHandler extends RequestHandler {

    final private Connection connection;
    final private ObjectOutputStream oos;
    final private AttemptExamRequest request;

    public AttemptExamRequestHandler(Connection connection, ObjectOutputStream oos, AttemptExamRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        try {
            PreparedStatement getQuestions = connection.prepareStatement(ExamTable.GET_EXAM_BY_EXAM_ID);
            getQuestions.setString(1, request.getExamId());
            ResultSet questionsSet = getQuestions.executeQuery();
            List<Question> questions = new ArrayList<>();
            while(questionsSet.next()) {
                questions.add(new Question(
                        questionsSet.getString(ExamQuestionsTable.QUESTION_ID_COLUMN),
                        questionsSet.getString(ExamQuestionsTable.QUESTION_COLUMN),
                        questionsSet.getString(ExamQuestionsTable.OPTION_A_COLUMN),
                        questionsSet.getString(ExamQuestionsTable.OPTION_B_COLUMN),
                        questionsSet.getString(ExamQuestionsTable.OPTION_C_COLUMN),
                        questionsSet.getString(ExamQuestionsTable.OPTION_D_COLUMN),
                        0 // Won't send correct option when questions are fetched for the exam. Checking will happen server-side.
                ));
            }
            AttemptExamResponse response = new AttemptExamResponse(questions, request.getExamId());
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null);
        }
    }
}
