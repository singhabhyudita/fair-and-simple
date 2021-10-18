package requestHandler;

import Classes.Exam;
import request.UpcomingExamsRequest;
import response.UpcomingExamsResponse;
import table.ExamTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import request.*;
import response.*;
import Classes.*;

public class ExamsHistoryRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    ExamsHistoryRequest request;
    String registrationNumber;

    public ExamsHistoryRequestHandler(Connection connection, ObjectOutputStream oos, ExamsHistoryRequest request,String registrationNumber) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public void sendResponse() {
        ExamsHistoryResponse examsHistoryResponse = null;
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(ExamTable.GET_EXAMS_HISTORY_STUDENT);
            preparedStatement.setString(1,registrationNumber);
            preparedStatement.setDate(2,new java.sql.Date(System.currentTimeMillis()));
            ResultSet results = preparedStatement.executeQuery();

            ArrayList<Exam> examsHistoryArrayList = new ArrayList<>();
            while(results.next())
            {
                Exam exam = new Exam(
                        results.getString(ExamTable.EXAM_ID_COLUMN), //examID
                        results.getString(ExamTable.PROCTOR_ID_COLUMN), //teacherId
                        results.getString(ExamTable.COURSE_ID_COLUMN), //courseId
                        results.getString(ExamTable.TITLE_COLUMN), //title
                        results.getDate(ExamTable.START_TIME_COLUMN), // startTime
                        results.getInt(ExamTable.MAXIMUM_MARKS_COLUMN)
                );
                examsHistoryArrayList.add(exam);
            }
            examsHistoryResponse = new ExamsHistoryResponse(examsHistoryArrayList);
        }
        catch (SQLException e)
        {
            System.out.println(" Error occurred while sending exams history list"+e.getMessage());
        }
        finally
        {
            try
            {
                oos.writeObject(examsHistoryResponse);
                oos.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
