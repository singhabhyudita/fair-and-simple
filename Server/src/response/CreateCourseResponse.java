package response;

public class CreateCourseResponse extends Response {
    private final String teamCode;

    public CreateCourseResponse(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamCode() {
        return teamCode;
    }
}
