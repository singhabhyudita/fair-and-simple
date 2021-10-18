package response;

public class CreateTeamResponse extends Response {
    private final String teamCode;

    public CreateTeamResponse(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamCode() {
        return teamCode;
    }
}
