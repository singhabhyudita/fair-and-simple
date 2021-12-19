package response;

public class ChangeTeacherProfilePicResponse extends Response {
    private String response;
    public ChangeTeacherProfilePicResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
