package requestHandler;

import main.Server;
import request.CreateCourseRequest;
import response.CreateCourseResponse;
import table.CoursesTable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateCourseRequestHandler {
    private final Connection connection;
    private final  ObjectOutputStream oos;
    private final CreateCourseRequest request;
    public CreateCourseRequestHandler(Connection connection, ObjectOutputStream oos, CreateCourseRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    public void sendResponse() {
        try {
            PreparedStatement createTeamStatement = connection.prepareStatement(CoursesTable.ADD_COURSE_QUERY);
            String teamCode = generateRandomCode();
            createTeamStatement.setString(1, request.getTeamName());
            createTeamStatement.setString(2, request.getTeamDescription());
            createTeamStatement.setString(3, teamCode);
            createTeamStatement.setString(4, request.getTeacherId());
            int result = createTeamStatement.executeUpdate();
            if(result == 0) Server.sendResponse(oos, null);
            else {
                System.out.println("Team created with code " + teamCode);
                System.out.println("Sending create team response\n");
                oos.writeObject(new CreateCourseResponse(teamCode));
                System.out.println("create team response sent.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Server.sendResponse(oos, null);
        }
    }

    private String generateRandomCode() throws SQLException {
        PreparedStatement coursesByCode = connection.prepareStatement(CoursesTable.GET_COURSES_BY_COURSE_CODE);
        while(true) {
            String current = Server.getRandomString();
            coursesByCode.setString(1, current);
            ResultSet coursesWithCurrentCode = coursesByCode.executeQuery();
            if(coursesWithCurrentCode.next()) continue;
            else return current;
        }
    }
}
