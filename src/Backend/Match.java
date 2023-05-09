package Backend;

import java.time.LocalDate;

public class Match {
    private final Team team1;
    private final Team team2;
    private int team1Score;
    private int team2Score;
    private LocalDate matchDate;
    private boolean isKnockout;
    private double team1GoalProb;
    private double team2GoalProb;
    private int minutes;

    public Match(Team teamOne, Team teamTwo){
        this(teamOne, teamTwo, LocalDate.now());
    }
    public Match (Team teamOne, Team teamTwo, int team1Score, int team2Score, LocalDate date) {
        this.team1 = teamOne;
        this.team2 = teamTwo;
        this.team1Score = 0;
        this.team2Score = 0;
        this.matchDate = date;
        this.isKnockout = false;
    }
    public Match (Team teamOne, Team teamTwo, LocalDate date) {
        this(teamOne, teamTwo, date, false);
    }
    public Match (Team teamOne, Team teamTwo, LocalDate date, boolean isKnockout) {
        this.team1 = teamOne;
        this.team2 = teamTwo;
        this.team1Score = 0;
        this.team2Score = 0;
        this.minutes = 0;
        this.matchDate = date;
        this.isKnockout = isKnockout;
    }

    /**
     * Randomly generates score values for 2 teams and then processes the match for any
     * overtime or tie dispute.
     */
    private double calculateGoalProbability(int rank){
        double goalProb = 0;
        if(rank < 52){
            goalProb = .750;
        }
        else if(rank < 104){
            goalProb = .775;
        }
        else if(rank < 156){
            goalProb = .800;
        }
        else {
            goalProb = .825;
        }
        return goalProb;
    }


    public void simulateMatchResult() {
        // Generate random scores for each team (0-4)
        // TODO: Adjust scoring calculation to more accurately simulate a game.

       final int matchDurationInMinutes = 90;
       final int extraTimeDurationInMinutes = 30;
       final int scoringIntervalInMinutes = 15;


       this.team1GoalProb = calculateGoalProbability(team1.getRank());
       this.team2GoalProb = calculateGoalProbability(team2.getRank());

       while(minutes < matchDurationInMinutes){
           minutes += scoringIntervalInMinutes;
           if(Math.random() > team1GoalProb){
               this.team1Score++;
           }
           if(Math.random() > team2GoalProb){
               this.team2Score++;
           }
       }

        // Check if match is in knockout stage
        // If match is taking place during the knockout stage and ends in a draw, execute tiebreaker procedure
        while (isKnockout && team1Score == team2Score) {
            // Simulate extra time being played, generate new scores for each team
            while(minutes < matchDurationInMinutes + extraTimeDurationInMinutes) {
                minutes += scoringIntervalInMinutes;
                if(Math.random() > team1GoalProb){
                    this.team1Score++;
                }
                if(Math.random() > team2GoalProb){
                    this.team2Score++;
                }
            }
            if (minutes < matchDurationInMinutes + extraTimeDurationInMinutes + 15) {
                // If there's still a tie after the extra time added, simulate penalty shootout

                for (int shots = 0; shots < 5; shots++) {
                    minutes += 3;
                    if(Math.random() > team1GoalProb + .1){
                        this.team1Score++;
                    }
                    if(Math.random() > team2GoalProb + .1){
                        this.team2Score++;
                    }
                }
                // If there's still a tie after the penalty shootout, simulate sudden death
            }
            else {
                minutes += scoringIntervalInMinutes;
                if(Math.random() > team1GoalProb){
                    this.team1Score++;
                }
                if(Math.random() > team2GoalProb){
                    this.team2Score++;
                }
            }
        }

        // Get the most recent scores for both teams
        int team1MostRecentScore = team1.getMostRecentScore();
        int team2MostRecentScore = team2.getMostRecentScore();


        // Update team points based on match result
        if (this.team1Score > this.team2Score){
            this.team1.setPointsOnDate(this.matchDate, team1MostRecentScore + 3);
        } else if (this.team1Score < this.team2Score){
            this.team2.setPointsOnDate(this.matchDate, team2MostRecentScore + 3);
        } else {
            this.team1.setPointsOnDate(this.matchDate, team1MostRecentScore + 1);
            this.team2.setPointsOnDate(this.matchDate, team2MostRecentScore + 1);
        }
    }

    public int getMinutes() {
        return minutes;
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
