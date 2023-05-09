package Backend;

import java.time.LocalDate;

public class Match {
    private final Team team1;
    private final Team team2;
    private int team1Score;
    private int team2Score;
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
        this.team1 = teamOne;
        this.team2 = teamTwo;
        team1Score = score1;
        team2Score = score2;
        this.matchDate = date;
    }
    public Match(Team teamOne, Team teamTwo){
        this(teamOne, teamTwo, LocalDate.now());
    }
    public Match (Team teamOne, Team teamTwo, LocalDate date) {
        this.team1 = teamOne;
        this.team2 = teamTwo;
        this.team1Score = 0;
        this.team2Score = 0;
        this.matchDate = date;
    }

    /**
     * Randomly generates score values for 2 teams and then processes the match for any
     * overtime or tie dispute.
     */
    public void simulateMatchResult() {
        // Generate random scores for each team (0-4)
        this.team1Score = (int) (Math.random() * 5);
        this.team2Score = (int) (Math.random() * 5);


        // Check if match is in knockout stage
        // TODO: this is not a safe way to determine knockout stage
        boolean isKnockoutActive = matchDate.getMonthValue() >= 6;

        // If match is taking place during the knockout stage and ends in a draw, execute tiebreaker procedure
        if (isKnockoutActive && team1Score == team2Score) {
            // Simulate extra time being played, generate new scores for each team
            int extraTimeTeam1Score = (int) (Math.random() * 2);
            int extraTimeTeam2Score = (int) (Math.random() * 2);
            this.team1Score += extraTimeTeam1Score;
            this.team2Score += extraTimeTeam2Score;
            // If there's still a tie after the extra time added, simulate penalty shootout
            if (team1Score == team2Score) {
                int pensTeam1Score = (int) (Math.random() * 5) + 1;
                int pensTeam2Score = (int) (Math.random() * 5) + 1;
                this.team1Score += pensTeam1Score;
                this.team2Score += pensTeam2Score;

                // If there's still a tie after the penalty shootout, simulate sudden death
                while (team1Score == team2Score) {
                    this.team1Score += (int) (Math.random() * 2);
                    this.team2Score += (int) (Math.random() * 2);
                }
            }
        }

        // Get the most recent scores for both teams
        int team1MostRecentScore = team1.getMostRecentScore();
        int team2MostRecentScore = team2.getMostRecentScore();


        // Update team points based on match result
        if (this.team1Score > this.team2Score){
            this.team1.setPoints(this.matchDate, team1MostRecentScore + 3);
        } else if (this.team1Score < this.team2Score){
            this.team2.setPoints(this.matchDate, team2MostRecentScore + 3);
        } else {
            this.team1.setPoints(this.matchDate, team1MostRecentScore + 1);
            this.team2.setPoints(this.matchDate, team2MostRecentScore + 1);
        }
    }

    public void setResult(int team1Score, int team2Score) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }

    public Team getTeam1(){
        return team1;
    }
    public Team getTeam2(){
        return team2;
    }
    public int getTeam1Score() {
        return team1Score;
    }
    public int getTeam2Score() {
        return team2Score;
    }
    public Team getLoser() {
        if (team1Score > team2Score) {
            return team2;
        } else if (team1Score < team2Score) {
            return team1;
        } else {
            return null;
        }
    }
    public Team getWinner() {
        if (team1Score > team2Score) {
            return team1;
        } else if (team1Score < team2Score) {
            return team2;
        } else {
            return null;
        }
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
                "team1=" + team1 +
                ", team2=" + team2 +
                ", team1Score=" + team1Score +
                ", team2Score=" + team2Score +
                ", matchDate=" + matchDate +
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
        return  team1.equals(match.team1) &&
                team2.equals(match.team2) &&
                team1Score == match.team1Score &&
                team2Score == match.team2Score &&
                matchDate.equals(match.matchDate);
    }

}
