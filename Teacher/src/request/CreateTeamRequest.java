package request;

public class CreateTeamRequest extends Request {
    private final String teacherId;
    private final String teamDescription;
    private final String teamName;

    public CreateTeamRequest(String teacherId, String teamDescription, String teamName) {
        this.teacherId = teacherId;
        this.teamDescription = teamDescription;
        this.teamName = teamName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public String getTeamName() {
        return teamName;
    }
}
