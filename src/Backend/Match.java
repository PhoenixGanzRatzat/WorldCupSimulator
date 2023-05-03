package Backend;

import java.time.LocalDate;

public class Match {
    private final Team team1;
    private final Team team2;
    private int team1Score;
    private int team2Score;
    private LocalDate matchDate;




    public Match(Team teamOne, Team teamTwo){
        this.team1 = teamOne;
        this.team2 = teamTwo;
    }

    public Match (Team teamOne, Team teamTwo, LocalDate date){
        this.team1 = teamOne;
        this.team2 = teamTwo;
        this.matchDate = date;
    }

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

    public void simulateMatchResult() {
        simulateMatchResult(MatchType.NO_MATCH_TYPE);
    }
    /**
     * Randomly generates score values for 2 teams and then processes the match for any
     * overtime or tie dispute.
     */
    public void simulateMatchResult(MatchType matchType) {
        // Generate random scores for each team (0-4)
        int team1Score = (int) (Math.random() * 5);
        int team2Score = (int) (Math.random() * 5);

        // Below is the Knockout Stage Tiebreaker code
        // If a tiebreaker is needed and the match ends in a draw during this stage, execute the tiebreaker procedure
        if (matchType == MatchType.KNOCKOUT && team1Score == team2Score) {
            // Simulate extra time being played, generate new scores
            int extraTimeTeam1Score = (int) (Math.random() * 2);
            int extraTimeTeam2Score = (int) (Math.random() * 2);
            team1Score += extraTimeTeam1Score;
            team2Score += extraTimeTeam2Score;

            // If there's still a tie after the extra time added, simulate penalty shootout
            if (team1Score == team2Score) {
                int penaltiesTeam1Score = (int) (Math.random() * 5) + 1;
                int penaltiesTeam2Score = (int) (Math.random() * 5) + 1;
                team1Score += penaltiesTeam1Score;
                team2Score += penaltiesTeam2Score;

                // If there's still a tie after the penalty shootout, simulate sudden death
                while (team1Score == team2Score) {
                    team1Score += (int) (Math.random() * 2);
                    team2Score += (int) (Math.random() * 2);
                }
            }
        }

        // Get the most recent scores for both teams
        int team1MostRecentScore = team1.getMostRecentPoints(matchDate);
        int team2MostRecentScore = team2.getMostRecentPoints(matchDate);

        // Update team points based on match result
        if (team1Score > team2Score) {
            team1.setPoints(matchDate, team1MostRecentScore + 3);
        } else if (team1Score < team2Score) {
            team2.setPoints(matchDate, team2MostRecentScore + 3);
        } else {
            team1.setPoints(matchDate, team1MostRecentScore + 1);
            team2.setPoints(matchDate, team2MostRecentScore + 1);
        }

        // Update the match object with the result (scores for both teams)
        setResult(team1Score, team2Score);


        // ADD: add a method that takes in a passed in boolean to check for ties.
        // If there is a tie, follow procedure: extra time, pens, and then sudden death





    }




    public void setResult(int team1Score, int team2Score) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }
    public Team getLosingTeam() {
        return team1;
    }

    public Team getWinningTeam() {
        return team2;
    }
    public int getTeam1Score() {
        return team1Score;
    }
    public Team getTeamOne() {
        return team1;
    }
    public Team getTeamTwo() {
        return team2;
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
    public Team getLoser() {
        if (team1Score > team2Score) {
            return team2;
        } else if (team1Score < team2Score) {
            return team1;
        } else {
            return null;
        }
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
        String text;
        if (team1Score > team2Score) {
            text = "Match{" +
                    "Winner =" + team1 +
                    ", Loser =" + team2 +
                    ", Winner Score =" + team1.getPoints(matchDate) +
                    ", teamTwoScore =" + team2.getPoints(matchDate)  +
                    '}';
        } else if (team1Score < team2Score) {
            text = "Match{" +
                    "Winner =" + team2 +
                    ", Loser =" + team1 +
                    ", Winner Score =" + team2.getPoints(matchDate) +
                    ", teamTwoScore =" + team1.getPoints(matchDate)  +
                    '}';
        } else {
            text = "Match{ Draw : " +
                    "Team 1 =" + team1 +
                    ", Team 2 =" + team2 +
                    ", Team 1 Score =" + team1.getPoints(matchDate) +
                    ", team 2 Score =" + team2.getPoints(matchDate)  +
                    '}';
        }

        return text;
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
