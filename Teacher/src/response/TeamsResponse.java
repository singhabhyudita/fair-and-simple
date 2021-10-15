package response;

import java.util.List;

public class TeamsResponse extends Response {
    private final List<Team> teams;

    public TeamsResponse(List<Team> teams) {
        this.teams = teams;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
