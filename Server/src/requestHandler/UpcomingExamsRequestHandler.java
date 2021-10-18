package requestHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;

import Classes.*;
import request.*;
import java.util.*;
import response.*;
import table.*;

public class UpcomingExamsRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    UpcomingExamsRequest request;
    String registrationNumber;
    public UpcomingExamsRequestHandler(Connection connection, ObjectOutputStream oos, UpcomingExamsRequest request,String registrationNumber) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public void sendResponse() {
        UpcomingExamsResponse upcomingExamsResponse = null;
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(ExamTable.GET_UPCOMING_EXAMS_STUDENT);
            preparedStatement.setString(1,registrationNumber);
            preparedStatement.setDate(2,new java.sql.Date(System.currentTimeMillis()));
            ResultSet results = preparedStatement.executeQuery();

            ArrayList <Exam> upcomingExamArrayList = new ArrayList<>();
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
                upcomingExamArrayList.add(exam);
            }
            upcomingExamsResponse = new UpcomingExamsResponse(upcomingExamArrayList);
        }
        catch (SQLException e)
        {
            System.out.println(" Error occurred while sending upcoming exams list"+e.getMessage());
        }
        finally
        {
            try
            {
                oos.writeObject(upcomingExamsResponse);
                oos.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
