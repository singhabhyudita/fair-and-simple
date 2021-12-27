package requestHandler;

import entity.Question;
import main.Server;
import request.GetResultForStudentRequest;
import response.GetResultForStudentResponse;
import table.AnswerFilesTable;
import table.ExamQuestionsMarksTable;
import table.ExamQuestionsTable;
import table.ObjectiveResponseTable;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetResultForStudentRequestHandler extends RequestHandler{

    private final Connection connection;
    private final ObjectOutputStream oos;
    private final GetResultForStudentRequest request;
    private boolean isCorrected = true;

    public GetResultForStudentRequestHandler(Connection connection, ObjectOutputStream oos, GetResultForStudentRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        System.out.println("Requested result for registration number = " + request.getStudentId());
        try {
            List<Question> questions = getQuestions();
            List<Question> objectiveAnswers = getObjectiveAnswers();
            byte [] subjectiveAnswers = getSubjectiveAnswers();
            Map<String, Integer> subjectiveQuestionMarks = getSubjectiveQuestionMarks(questions, request.getStudentId());
            checkIfCorrected();
            Server.sendResponse(oos, new GetResultForStudentResponse(questions,
                    objectiveAnswers,
                    subjectiveAnswers,
                    subjectiveQuestionMarks,
                    isCorrected));
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null);
        }
    }

    private void checkIfCorrected() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ExamQuestionsMarksTable.GET_ENTRY_BY_EXAM_ID_AND_REGISTRATION_NO);
        statement.setString(1, request.getExamId());
        statement.setString(2, request.getStudentId());
        ResultSet set = statement.executeQuery();
        isCorrected = set.next();
    }

    private Map<String, Integer> getSubjectiveQuestionMarks(List<Question> questions, String studentId) {
        Map<String, Integer> subjectiveQuestionMarks = new HashMap<>();
        try {
            PreparedStatement statement = connection.prepareStatement(ExamQuestionsMarksTable.GET_ENTRY_BY_QUESTION_ID);
            for(Question question : questions) {
                if (!question.isObjective()) {
                    statement.setString(1, question.getQuestionId());
                    statement.setString(2, studentId);
                    ResultSet set = statement.executeQuery();
                    if(set.next()) {
                        subjectiveQuestionMarks.put(question.getQuestionId(), set.getInt(ExamQuestionsMarksTable.COLUMN_CORRECT));
                    } else {
                        isCorrected = false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjectiveQuestionMarks;
    }

    private byte[] getSubjectiveAnswers() {
        try {
            PreparedStatement statement = connection.prepareStatement(AnswerFilesTable.GET_ENTRY_BY_EXAM_ID_REGISTRATION_NO);
            statement.setString(1, request.getExamId());
            statement.setString(2, request.getStudentId());
            ResultSet filePathSet = statement.executeQuery();
            if (filePathSet.next()) {
                String filePath = filePathSet.getString(AnswerFilesTable.COLUMN_ANSWER_PATH);
                File file = new File(filePath);
                FileInputStream stream = new FileInputStream(file);
                byte [] buff = new byte[(int) file.length()];
                stream.read(buff);
                stream.close();
                return buff;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Question> getQuestions() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ExamQuestionsTable.GET_EXAM_QUESTIONS_BY_EXAM_ID);
        statement.setString(1, request.getExamId());
        ResultSet questionSet = statement.executeQuery();
        List<Question> questions = new ArrayList<>();
        while (questionSet.next()) {
            questions.add(new Question(
                    questionSet.getString(ExamQuestionsTable.QUESTION_ID_COLUMN),
                    questionSet.getString(ExamQuestionsTable.QUESTION_COLUMN),
                    questionSet.getString(ExamQuestionsTable.OPTION_A_COLUMN),
                    questionSet.getString(ExamQuestionsTable.OPTION_B_COLUMN),
                    questionSet.getString(ExamQuestionsTable.OPTION_C_COLUMN),
                    questionSet.getString(ExamQuestionsTable.OPTION_D_COLUMN),
                    questionSet.getInt(ExamQuestionsTable.CORRECT_OPTION_COLUMN),
                    questionSet.getInt(ExamQuestionsTable.CORRECT_OPTION_COLUMN) != -1
            ));
        }
        return questions;
    }

    private List<Question> getObjectiveAnswers() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ObjectiveResponseTable.GET_ENTRY_BY_EXAM_ID_AND_REGISTRATION_NO);
        statement.setString(1, request.getExamId());
        statement.setString(2, request.getStudentId());
        ResultSet objectiveResponses = statement.executeQuery();
        List<Question> answers = new ArrayList<>();
        while(objectiveResponses.next()) {
            answers.add(new Question(
                    objectiveResponses.getString(ObjectiveResponseTable.COLUMN_QUESTION_ID),
                    "",
                    "",
                    "",
                    "",
                    "",
                    objectiveResponses.getInt(ObjectiveResponseTable.COLUMN_MARKED),
                    true
            ));
        }
        return answers;
    }
}
