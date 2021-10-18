package Response;

import java.io.Serializable;

public class LogOutResponse extends Response implements Serializable {
    private String response;
    LogOutResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
