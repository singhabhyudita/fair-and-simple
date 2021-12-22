package requestHandler;

import request.ExamResultRequest;

import java.io.ObjectOutputStream;
import java.sql.Connection;

public class ExamResultRequestHandler extends RequestHandler {
    private final Connection connection;
    private final ObjectOutputStream oos;
    private final ExamResultRequest request;
    public ExamResultRequestHandler(Connection connection, ObjectOutputStream oos, ExamResultRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.request = request;
    }

    public void sendResponse(String userID) {
    }
}
