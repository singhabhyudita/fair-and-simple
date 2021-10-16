package requestHandler;

import main.Server;
import request.SetExamRequest;
import response.Question;
import response.SetExamResponse;
import response.Status;
import table.ExamQuestionsTable;
import table.ExamTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SetExamRequestHandler {
    private final Connection connection;
    private final ObjectOutputStream oos;
    private final SetExamRequest request;
    public SetExamRequestHandler(Connection connection, ObjectOutputStream oos, SetExamRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    public void sendResponse() {
        try {
            PreparedStatement getClashingExams = connection.prepareStatement(ExamTable.GET_CLASHING_EXAMS_BY_TEACHER);
            getClashingExams.setString(1, request.getTeacherId());
            getClashingExams.setObject(2, request.getEndTime());
            getClashingExams.setObject(3, request.getStartTime());
            ResultSet resultSet = getClashingExams.executeQuery();
            if(resultSet.next()) {
                Server.sendResponse(oos, new SetExamResponse(Status.CLASH));
                return;
            }

            PreparedStatement setExam = connection.prepareStatement(ExamTable.ADD_EXAM_DETAILS);
            setExam.setString(1, request.getCourseId());
            setExam.setString(2, request.getTeacherId());
            setExam.setString(3, request.getExamTitle());
            setExam.setString(4, request.getExamTitle()); // temporary.
            setExam.setString(5, String.valueOf(request.getQuestions().size()));
            setExam.setObject(6, request.getStartTime());
            setExam.setObject(7, request.getEndTime());
            int result = setExam.executeUpdate();
            if(result == 0) {
                Server.sendResponse(oos, new SetExamResponse(Status.OTHER));
                return;
            }

            String examId = getExamId();
            if(examId == null) {
                Server.sendResponse(oos, new SetExamResponse(Status.OTHER));
                return;
            }

            PreparedStatement addQuestions = connection.prepareStatement(ExamQuestionsTable.ADD_QUESTION_QUERY);
            for(Question question : request.getQuestions()) {
                addQuestions.setString(1, examId);
                addQuestions.setString(2, question.getQuestion());
                addQuestions.setString(3, question.getOptionA());
                addQuestions.setString(4, question.getOptionB());
                addQuestions.setString(5, question.getOptionC());
                addQuestions.setString(6, question.getOptionD());
                addQuestions.setString(7, String.valueOf(question.getCorrectOption()));
                int questionAdded = addQuestions.executeUpdate();
                if(questionAdded == 0) {
                    deleteExamByID(examId);
                    Server.sendResponse(oos, new SetExamResponse(Status.OTHER));
                    return;
                }
            }
            Server.sendResponse(oos, new SetExamResponse(Status.EXAM_CREATED));
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, new SetExamResponse(Status.OTHER));
        }
    }

    private String getExamId() throws SQLException {
        PreparedStatement getExamId = connection.prepareStatement(ExamTable.GET_EXAM_ID);
        getExamId.setString(1, request.getTeacherId());
        getExamId.setObject(2, request.getStartTime());
        getExamId.setObject(3, request.getEndTime());
        ResultSet exam = getExamId.executeQuery();
        if(exam.next()) return exam.getString(ExamTable.EXAM_ID_COLUMN);
        else return null;
    }

    private void deleteExamByID(String examId) throws SQLException {
        PreparedStatement deleteQuestionByExam = connection.prepareStatement(ExamQuestionsTable.DELETE_QUESTIONS_BY_EXAM_ID);
        deleteQuestionByExam.setObject(1, examId);
        deleteQuestionByExam.executeUpdate();
        PreparedStatement deleteExam = connection.prepareStatement(ExamTable.DELETE_EXAM_BY_ID);
        deleteExam.setString(1, examId);
        deleteExam.executeUpdate();
    }
}
