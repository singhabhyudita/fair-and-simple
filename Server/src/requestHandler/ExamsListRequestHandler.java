package requestHandler;

import entity.Exam;
import request.ExamsListRequest;
import response.ExamsListResponse;
import table.ExamTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExamsListRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private ExamsListRequest examsListRequest;

    public ExamsListRequestHandler(Connection connection, ObjectOutputStream oos, ExamsListRequest examsListRequest) {
        this.connection = connection;
        this.oos = oos;
        this.examsListRequest = examsListRequest;
    }

    @Override
    public void sendResponse() {
        ArrayList<Exam>exams=new ArrayList<>();
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(ExamTable.GET_EXAM_BY_COURSE_ID);
            preparedStatement.setInt(1,Integer.parseInt(examsListRequest.getCourseId()));
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                exams.add(new Exam(resultSet.getString(ExamTable.EXAM_ID_COLUMN),resultSet.getString(ExamTable.TEACHER_ID_COLUMN)
                ,resultSet.getString(ExamTable.COURSE_ID_COLUMN), resultSet.getString(ExamTable.PROCTOR_ID_COLUMN),resultSet.getString(ExamTable.COURSE_ID_COLUMN),resultSet.getString(ExamTable.TITLE_COLUMN), resultSet.getString(ExamTable.DESCRIPTION_COLUMN),
                        resultSet.getTimestamp(ExamTable.START_TIME_COLUMN), resultSet.getTimestamp(ExamTable.END_TIME_COLUMN),resultSet.getInt(ExamTable.MAXIMUM_MARKS_COLUMN)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(new ExamsListResponse(exams));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
