package requestHandler;

import entity.Question;
import entity.Status;
import main.Server;
import request.SetExamRequest;
import response.SetExamResponse;
import table.ExamQuestionsTable;
import table.ExamTable;
import table.TeacherTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SetExamRequestHandler extends RequestHandler{
    private final Connection connection;
    private final ObjectOutputStream oos;
    private final SetExamRequest request;
    public SetExamRequestHandler(Connection connection, ObjectOutputStream oos, SetExamRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    public void sendResponse(String userID) {
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(TeacherTable.GET_TEACHER_NAME_BY_ID);
            preparedStatement.setInt(1,request.getProctorId());
            ResultSet query=preparedStatement.executeQuery();
            if(!query.next()){
                Server.sendResponse(oos,new SetExamResponse(Status.PROCTOR_INVALID));
                return;
            }

            PreparedStatement proctorClash=connection.prepareStatement(ExamTable.GET_CLASHING_EXAMS_FOR_PROCTOR);
            proctorClash.setInt(1,request.getProctorId());
            proctorClash.setObject(2,request.getEndTime());
            proctorClash.setObject(3,request.getStartTime());
            ResultSet getClash=proctorClash.executeQuery();
            if(getClash.next()){
                Server.sendResponse(oos,new SetExamResponse(Status.PROCTOR_UNAVAILABLE));
                return;
            }
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
            setExam.setInt(2, request.getProctorId());
            setExam.setString(3, request.getExamTitle());
            setExam.setString(4, request.getDescription());
            setExam.setString(5, String.valueOf(request.getQuestions().size()));
            setExam.setObject(6, request.getStartTime());
            setExam.setObject(7, request.getEndTime());
            setExam.setString(8, request.getTeacherId());

            System.out.println("Set exam query");
            System.out.println(setExam);
            int result = setExam.executeUpdate();
            if(result == 0) {
                System.out.println("Returning from here 1111111");
                Server.sendResponse(oos, new SetExamResponse(Status.OTHER));
                return;
            }

            String examId = getExamId();
            if(examId == null) {
                System.out.println("Returning from here 2222222");
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
                    System.out.println("Returning from here 3333333");
                    Server.sendResponse(oos, new SetExamResponse(Status.OTHER));
                    return;
                }
            }
            Server.sendResponse(oos, new SetExamResponse(Status.EXAM_CREATED));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Returning from here 4444444");
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
