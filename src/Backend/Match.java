package Backend;

public class Match {
    private Team teamOne;
    private Team teamTwo;
    private int teamOneScore;
    private int teamTwoScore;

    public Match(Team teamOne, Team teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    public Match(Team teamOne, Team teamTwo, int scoreOne, int scoreTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        teamOneScore = scoreOne;
        teamTwoScore = scoreTwo;
    }

    public Team getTeamOne() {
        return teamOne;
    }

    public Team getTeamTwo() {
        return teamTwo;
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }
}
