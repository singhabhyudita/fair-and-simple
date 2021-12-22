package requestHandler;

import entity.Exam;
import main.RequestIdentifier;
import main.Server;
import request.ProctoringDutyRequest;
import response.ProctoringDutyResponse;
import table.CoursesTable;
import table.ExamTable;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProctoringDutyRequestHandler extends RequestHandler {
    final private Connection connection;
    final private ObjectOutputStream oos;
    final private ProctoringDutyRequest request;

    public ProctoringDutyRequestHandler(Connection connection, ObjectOutputStream oos, ProctoringDutyRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ExamTable.GET_PROCTORING_DUTY_BY_TEACHER_ID);
            preparedStatement.setString(1, userID);
            ResultSet results = preparedStatement.executeQuery();
            List<Exam> exams = new ArrayList<>();
            while(results.next()) {
                String courseName = null;
                String courseId=results.getString(ExamTable.COURSE_ID_COLUMN);
                preparedStatement=connection.prepareStatement(CoursesTable.GET_COURSE_NAME_BY_COURSE_ID);
                preparedStatement.setString(1,courseId);
                ResultSet set=preparedStatement.executeQuery();
                set.next();
                courseName=set.getString(1);
                Exam exam = new Exam(
                        results.getString(ExamTable.EXAM_ID_COLUMN), //examID
                        results.getString(ExamTable.TEACHER_ID_COLUMN), //teacherId
                        courseId,
                        results.getString(ExamTable.PROCTOR_ID_COLUMN),
                        courseName,
                        results.getString(ExamTable.TITLE_COLUMN), //title
                        results.getString(ExamTable.DESCRIPTION_COLUMN),
                        results.getTimestamp(ExamTable.START_TIME_COLUMN), // startTime
                        results.getTimestamp(ExamTable.END_TIME_COLUMN),
                        results.getInt(ExamTable.MAXIMUM_MARKS_COLUMN)
                );
                exams.add(exam);
            }
            ProctoringDutyResponse response = new ProctoringDutyResponse(exams);
            Server.sendResponse(oos, response);
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null);
        }
    }
}
