package Backend;

import java.time.LocalDate;

public class Match {

    private Team winner;
    private Team loser;
    private int team1Score;
    private int team2Score;
    private LocalDate matchDate;


    public Match(Team teamOne, Team teamTwo){
        this.winner = teamOne;
        this.loser = teamTwo;
    }

    public Match (Team teamOne, Team teamTwo, LocalDate date){
        this.winner = teamOne;
        this.loser = teamTwo;
        this.matchDate = date;
    }

    public Team getTeamOne() {
        return winner;
    }

    public void setResult(int team1Score, int team2Score) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public MatchResult simulateMatchResult(MatchType matchType) {
        // Generate random scores for each team (0-4)
        int team1Score = (int) (Math.random() * 5);
        int team2Score = (int) (Math.random() * 5);

        // Update team points based on match result
        if (team1Score > team2Score) {
            // If team1 wins, add 3 points to their qualifier points
            winner.setQualifierPoints(winner.getQualifierPoints() + 3);

        } else if (team1Score < team2Score) {
            // If team2 wins, add 3 points to their qualifier points
            loser.setQualifierPoints(loser.getQualifierPoints() + 3);

            // Swap the winner and loser teams in case
            Team tempTeam = winner;
            winner = loser;
            loser = tempTeam;
        } else {
            // If the match ends in a draw, add 1 point to both teams' qualifier points
            winner.setQualifierPoints(winner.getQualifierPoints() + 1);
            loser.setQualifierPoints(loser.getQualifierPoints() + 1);
        }

        // ADD: add a method that takes in a passed in boolean to check for ties.
        // If there is a tie, follow procedure: extra time, pens, and then sudden death


        // Update the match object with the result (scores for both teams)
        setResult(team1Score, team2Score);
        return new MatchResult(winner, loser, team1Score, team2Score);
    }

    public MatchResult simulateRoundOfSixteenMatch() {
        return simulateMatchResult(MatchType.ROUND_OF_SIXTEEN);
    }

    public MatchResult simulateQuarterfinalsMatch() {
        return simulateMatchResult(MatchType.QUARTERFINALS);
    }

    public MatchResult simulateSemifinalsMatch() {
        return simulateMatchResult(MatchType.SEMIFINALS);
    }

    public MatchResult simulateFinals() {
        return simulateMatchResult(MatchType.FINALS);
    }

    public Team getLosingTeam() {
        return loser;
    }

    public Team getWinningTeam() {
        return winner;
    }
    public int getTeam1Score() {
        return team1Score;
    }

    public Team getTeamTwo() {
        return loser;
    }

    public int getTeamOneScore() {
        return team1Score;
    }

    public int getTeamTwoScore() {
        return team2Score;
    }
    // TODO: Add appropriate accessors/mutators for Dates (getDate/setDate)
    public LocalDate getMatchDate(){
        return matchDate;
    }

    public void setMatchDate(LocalDate desiredDate){
        this.matchDate = desiredDate;
    }


    @Override
    public String toString() {
        return "Match{" +
                "Winner =" + winner +
                ", Loser =" + loser +
                ", Winner Score =" + winner.getQualifierPoints() +
                ", teamTwoScore=" + loser.getQualifierPoints()  +
                '}';
    }
}
