package Backend;

import java.time.LocalDate;

public class Match {
    private Team teamOne;
    private Team teamTwo;
    private int teamOneScore;
    private int teamTwoScore;
    private LocalDate date;

    public Match(Team teamOne, Team teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    public Match(Team teamOne, Team teamTwo, int scoreOne, int scoreTwo, LocalDate date){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        teamOneScore = scoreOne;
        teamTwoScore = scoreTwo;
        this.date = date;
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

    public LocalDate getMatchDate() {
        return this.date;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Match match = (Match) obj;
        return  teamOne.equals(match.teamOne) &&
                teamTwo.equals(match.teamTwo) &&
                teamOneScore == match.teamOneScore &&
                teamTwoScore == match.teamTwoScore &&
                date.equals(match.date);
    }
}
