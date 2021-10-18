package response;

import java.io.InputStream;
import java.io.Serializable;

public class GetProfilePicResponse extends Response implements Serializable {
    InputStream inputStream;

    public GetProfilePicResponse(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
