package requestHandler;

import entity.Question;
import request.GetQuestionsRequest;
import response.GetQuestionsResponse;
import table.AnswerFilesTable;
import table.ExamQuestionsTable;
import table.ObjectiveResponseTable;
import table.ProctorPortTable;

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
    public void sendResponse(String userID) {
        ArrayList<Question> questions=new ArrayList<>();
        ResultSet resultSet;
        PreparedStatement preparedStatement= null;
        GetQuestionsResponse getQuestionsResponse;
        boolean alreadyAttempted = false;
        int proctorPort = -1;
        try {
            preparedStatement = connection.prepareStatement(ProctorPortTable.GET_PORT_BY_EXAM_ID);
            preparedStatement.setString(1, getQuestionsRequest.getExamId());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                proctorPort = resultSet.getInt(ProctorPortTable.COLUMN_PROCTOR_PORT);
            }
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
                                resultSet.getInt(ExamQuestionsTable.CORRECT_OPTION_COLUMN),
                            resultSet.getInt(ExamQuestionsTable.CORRECT_OPTION_COLUMN) != -1
                    ));
            }
            preparedStatement = connection.prepareStatement(ObjectiveResponseTable.GET_ENTRY_BY_EXAM_ID_AND_REGISTRATION_NO);
            preparedStatement.setString(1, getQuestionsRequest.getExamId());
            preparedStatement.setString(2, userID);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) alreadyAttempted = true;
            if(!alreadyAttempted) {
                preparedStatement = connection.prepareStatement(AnswerFilesTable.GET_ENTRY_BY_EXAM_ID_REGISTRATION_NO);
                preparedStatement.setString(1, getQuestionsRequest.getExamId());
                preparedStatement.setString(2, userID);
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) alreadyAttempted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("sending questions array "+ questions.get(0).getOptionA());
        try {
            getQuestionsResponse=new GetQuestionsResponse(questions, proctorPort, alreadyAttempted);
            System.out.println("Get questions resposne");
            oos.writeObject(getQuestionsResponse);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
