package requestHandler;

import request.GetTeacherProfilePicRequest;

import java.io.ObjectOutputStream;
import java.sql.Connection;

public class GetTeacherProfilePicRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private GetTeacherProfilePicRequest getTeacherProfilePicRequest;

    public GetTeacherProfilePicRequestHandler(Connection connection, ObjectOutputStream oos, GetTeacherProfilePicRequest getTeacherProfilePicRequest) {
        this.connection = connection;
        this.oos = oos;
        this.getTeacherProfilePicRequest = getTeacherProfilePicRequest;
    }

    @Override
    public void sendResponse() {

    }
}
