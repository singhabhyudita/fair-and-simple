package requestHandler;
import entity.Exam;
import main.Server;
import request.TeacherExamRequest;
import response.TeacherExamResponse;
import table.CoursesTable;
import table.ExamTable;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherExamRequestHandler {
    private final Connection connection;
    private final ObjectOutputStream oos;
    private final TeacherExamRequest request;
    public TeacherExamRequestHandler(Connection connection, ObjectOutputStream oos, TeacherExamRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    public void sendResponse() {
        try {
            PreparedStatement getAllExams = connection.prepareStatement(ExamTable.GET_EXAM_BY_TEACHER_ID);
            getAllExams.setString(1, request.getTeacherId());
            ResultSet allExams = getAllExams.executeQuery();
            List<Exam> exams = new ArrayList<>();
            while(allExams.next()) {
                if(request.isPastOnly() && allExams.getDate(ExamTable.START_TIME_COLUMN).after(new Date()))
                    continue;
                exams.add(new Exam(allExams.getString(ExamTable.EXAM_ID_COLUMN),
                        allExams.getString(ExamTable.TEACHER_ID_COLUMN),
                        allExams.getString(ExamTable.COURSE_ID_COLUMN),
                        allExams.getString(ExamTable.PROCTOR_ID_COLUMN),
                        allExams.getString(CoursesTable.COURSE_NAME_COLUMN),
                        allExams.getString(ExamTable.TITLE_COLUMN),
                        allExams.getString(ExamTable.DESCRIPTION_COLUMN),
                        allExams.getTimestamp(ExamTable.START_TIME_COLUMN),
                        allExams.getTimestamp(ExamTable.END_TIME_COLUMN),
                        allExams.getInt(ExamTable.MAXIMUM_MARKS_COLUMN)
                        ));
            }
            Server.sendResponse(oos, new TeacherExamResponse(exams));
        } catch (SQLException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null);
        }
    }
}
