package requestHandler;

import request.ChangeTeacherProfilePicRequest;

import java.io.ObjectOutputStream;
import java.sql.Connection;

public class ChangeTeacherProfilePicRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private ChangeTeacherProfilePicRequest changeTeacherProfilePicRequest;

    public ChangeTeacherProfilePicRequestHandler(Connection connection, ObjectOutputStream oos, ChangeTeacherProfilePicRequest changeTeacherProfilePicRequest) {
        this.connection = connection;
        this.oos = oos;
        this.changeTeacherProfilePicRequest = changeTeacherProfilePicRequest;
    }

    @Override
    public void sendResponse() {

    }
}
