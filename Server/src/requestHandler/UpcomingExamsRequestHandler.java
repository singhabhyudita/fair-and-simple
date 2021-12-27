package requestHandler;

import entity.Exam;
import request.UpcomingExamsRequest;
import response.UpcomingExamsResponse;
import table.CoursesTable;
import table.ExamTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UpcomingExamsRequestHandler extends RequestHandler {
    Connection connection;
    ObjectOutputStream oos;
    UpcomingExamsRequest request;
    public UpcomingExamsRequestHandler(Connection connection, ObjectOutputStream oos, UpcomingExamsRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    @Override
    public void sendResponse(String userID) {
        UpcomingExamsResponse upcomingExamsResponse = null;
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(ExamTable.GET_UPCOMING_EXAMS_STUDENT);
            preparedStatement.setString(1, userID);
            preparedStatement.setTimestamp(2,new java.sql.Timestamp(System.currentTimeMillis()));
            ResultSet results = preparedStatement.executeQuery();

            ArrayList <Exam> upcomingExamArrayList = new ArrayList<>();
            while(results.next())
            {
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
