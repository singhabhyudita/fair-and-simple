package requestHandler;

import main.RequestIdentifier;
import request.JoinCourseRequest;
import response.JoinCourseResponse;
import table.CoursesTable;
import table.EnrollmentTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinCourseRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private JoinCourseRequest joinCourseRequest;

    public JoinCourseRequestHandler(Connection connection, ObjectOutputStream oos, JoinCourseRequest joinCourseRequest) {
        this.connection = connection;
        this.oos = oos;
        this.joinCourseRequest = joinCourseRequest;
    }

    @Override
    public void sendResponse()  {
        ResultSet resultSet = null;
        int result=0;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(CoursesTable.GET_COURSE_ID_BY_COURSE_CODE);
            preparedStatement.setInt(1,Integer.parseInt(joinCourseRequest.getCourseCode()));
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                preparedStatement=connection.prepareStatement(EnrollmentTable.QUERY_JOIN_COURSE_BY_ID);
                preparedStatement.setInt(1,resultSet.getInt(1));
                preparedStatement.setInt(2,Integer.parseInt(RequestIdentifier.userID));
                result=preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(result==0)oos.writeObject(new JoinCourseResponse("",""));
            else oos.writeObject(new JoinCourseResponse("Successful",String.valueOf(resultSet.getInt(1))));
            oos.flush();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
