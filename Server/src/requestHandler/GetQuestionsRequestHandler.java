package requestHandler;

import entity.Question;
import request.GetQuestionsRequest;
import response.GetQuestionsResponse;
import table.ExamQuestionsTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetQuestionsRequestHandler extends RequestHandler{
    private Connection connection;
    private ObjectOutputStream oos;
    private GetQuestionsRequest getQuestionsRequest;

    public GetQuestionsRequestHandler(Connection connection, ObjectOutputStream oos, GetQuestionsRequest getQuestionsRequest) {
        this.connection = connection;
        this.oos = oos;
        this.getQuestionsRequest = getQuestionsRequest;
    }

    @Override
    public void sendResponse() {
        ArrayList<Question> questions=new ArrayList<>();
        ResultSet resultSet;
        PreparedStatement preparedStatement= null;
        GetQuestionsResponse getQuestionsResponse;
        try {
            preparedStatement = connection.prepareStatement(ExamQuestionsTable.GET_EXAM_QUESTIONS_BY_EXAM_ID);
            preparedStatement.setInt(1,Integer.parseInt(getQuestionsRequest.getExamId()));
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()) {
                    questions.add(new Question(
                                resultSet.getString(ExamQuestionsTable.QUESTION_ID_COLUMN),
                                resultSet.getString(ExamQuestionsTable.QUESTION_COLUMN),
                                resultSet.getString(ExamQuestionsTable.OPTION_A_COLUMN),
                                resultSet.getString(ExamQuestionsTable.OPTION_B_COLUMN),
                                resultSet.getString(ExamQuestionsTable.OPTION_C_COLUMN),
                                resultSet.getString(ExamQuestionsTable.OPTION_D_COLUMN),
                                resultSet.getInt(ExamQuestionsTable.CORRECT_OPTION_COLUMN)
                            ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("sending questions array "+ questions.get(0).getOptionA());
        try {
            getQuestionsResponse=new GetQuestionsResponse(questions);
            oos.writeObject(getQuestionsResponse);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
