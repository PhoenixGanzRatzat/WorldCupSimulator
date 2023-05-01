package Backend;

public class MatchResult {
    private final Team winningTeam;
    private final Team losingTeam;
    private final int winningTeamScore;
    private final int losingTeamScore;

    public MatchResult(Team winningTeam, Team losingTeam, int winningTeamScore, int losingTeamScore) {
        this.winningTeam = winningTeam;
        this.losingTeam = losingTeam;
        this.winningTeamScore = winningTeamScore;
        this.losingTeamScore = losingTeamScore;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public Team getLosingTeam() {
        return losingTeam;
    }
}
