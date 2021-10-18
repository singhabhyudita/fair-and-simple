package Response;
import java.io.Serializable;
import Classes.*;

public class JoinCourseResponse extends Response implements Serializable {
    String response;
    public JoinCourseResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
