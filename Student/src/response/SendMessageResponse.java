package response;

public class SendMessageResponse extends Response {
    private final String response;

    public SendMessageResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
