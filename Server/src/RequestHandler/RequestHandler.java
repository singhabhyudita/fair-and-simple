package RequestHandler;

import java.sql.SQLException;

public abstract class RequestHandler {
    public abstract void sendResponse() throws SQLException;
}
