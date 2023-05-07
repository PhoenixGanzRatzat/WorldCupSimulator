package Backend;

import java.time.LocalDate;

public class Match {
    private final Team winner;
    private final Team loser;
    private int teamOneScore;
    private int teamTwoScore;
    private LocalDate matchDate;

    /**
     * TEMPORARY - used for groupPanel testing, delete later.
     * @param teamOne
     * @param teamTwo
     * @param score1
     * @param score2
     * @param date
     */
    public Match (Team teamOne, Team teamTwo, int score1, int score2, LocalDate date){
        this.winner = teamOne;
        this.loser = teamTwo;
        teamOneScore = score1;
        teamTwoScore = score2;
        this.matchDate = date;
    }
    public Match(Team teamOne, Team teamTwo){
        this(teamOne, teamTwo, LocalDate.now());
    }
    public Match (Team teamOne, Team teamTwo, LocalDate date) {
        this.matchDate = date;
        simulateMatchResult();

        if (teamOneScore > teamTwoScore) {
            this.winner = teamOne;
            this.loser = teamTwo;
        }
        else{
            this.winner = teamTwo;
            this.loser = teamOne;
        }

        assignPointsToTeams();
    }

    /**
     * Randomly generates score values for 2 teams and then processes the match for any
     * overtime or tie dispute.
     */
    public void simulateMatchResult() {
        // Generate random scores for each team (0-4)
        this.teamOneScore = (int) (Math.random() * 5);
        this.teamTwoScore = (int) (Math.random() * 5);

        // Check if match is in knockout stage
        boolean isKnockoutActive = matchDate.getMonthValue() >= 6;

        // If match is taking place during the knockout stage and ends in a draw, execute tiebreaker procedure
        if (isKnockoutActive && teamOneScore == teamTwoScore) {
            // Simulate extra time being played, generate new scores for each team
            int extraTimeTeam1Score = (int) (Math.random() * 2);
            int extraTimeTeam2Score = (int) (Math.random() * 2);
            this.teamOneScore += extraTimeTeam1Score;
            this.teamTwoScore += extraTimeTeam2Score;

            // If there's still a tie after the extra time added, simulate penalty shootout
            if (teamOneScore == teamTwoScore) {
                int pensTeam1Score = (int) (Math.random() * 5) + 1;
                int pensTeam2Score = (int) (Math.random() * 5) + 1;
                this.teamOneScore += pensTeam1Score;
                this.teamTwoScore += pensTeam2Score;

                // If there's still a tie after the penalty shootout, simulate sudden death
                while (teamOneScore == teamTwoScore) {
                    this.teamOneScore += (int) (Math.random() * 2);
                    this.teamTwoScore += (int) (Math.random() * 2);
                }
            }
        }
    }

    private void assignPointsToTeams(){
        // Get the most recent scores for both teams
        int winnerMostRecentScore = winner.getMostRecentScore(matchDate);
        int loserMostRecentScore = loser.getMostRecentScore(matchDate);


        // Update team points based on match result
        if (this.teamOneScore != this.teamTwoScore){
            winner.setPoints(matchDate, winnerMostRecentScore + 3);
            loser.setPoints(this.matchDate, loserMostRecentScore + 3);
        }
        else{
            this.winner.setPoints(this.matchDate, winnerMostRecentScore + 1);
            this.loser.setPoints(this.matchDate, loserMostRecentScore + 1);
        }
    }

    public void setResult(int team1Score, int team2Score) {
        this.teamOneScore = team1Score;
        this.teamTwoScore = team2Score;
    }

    public Team getWinner() {
        return winner;
    }
    public Team getLoser() {
        return loser;
    }
    public int getWinningScore() {
        return (Math.max(teamOneScore, teamTwoScore));
    }
    public int getLosingScore() {
        return (Math.min(teamOneScore, teamTwoScore));
    }
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
                ", Winning Score =" + getWinningScore() +
                ", Losing Score =" + getLosingScore() +
                ", matchDate =" + matchDate +
                '}';
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
        return  winner.equals(match.winner) &&
                loser.equals(match.loser) &&
                teamOneScore == match.teamOneScore &&
                teamTwoScore == match.teamTwoScore &&
                matchDate.equals(match.matchDate);
    }

}
