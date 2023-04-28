package Backend;

import java.util.HashMap;

public class Match {
    private Team team1;
    private Team team2;

    private int team1Score;
    private int team2Score;

    private HashMap<String, Team>  matchTeams;

    public Match(){
        this.matchTeams = new HashMap<>();
    }
    public Match(Team team1, Team team2){
        this();
       addTeam(team1);
       addTeam(team2);
    }

    public void addTeam(Team team) {
        if (matchTeams.size() < 2) {
            matchTeams.put(team.getAbbv(), team);
        } else {
            System.out.println("Error: Each match can only have two teams.");
        }
    }
    public void setResult(int team1Score, int team2Score) {
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void simulateMatchResult() {
        // Get both teams from the current match
        Team team1 = getTeam(getTeamsInMatch().keySet().toArray(new String[0])[0]);
        Team team2 = getTeam(getTeamsInMatch().keySet().toArray(new String[0])[1]);

        // Generate random scores for each team (0-4)
        int team1Score = (int) (Math.random() * 5);
        int team2Score = (int) (Math.random() * 5);

        // Update team points based on match result
        if (team1Score > team2Score) {
            // If team1 wins, add 3 points to their qualifier points
            team1.setQualifierPoints(team1.getQualifierPoints() + 3);
        } else if (team1Score < team2Score) {
            // If team2 wins, add 3 points to their qualifier points
            team2.setQualifierPoints(team2.getQualifierPoints() + 3);
        } else {
            // If the match ends in a draw, add 1 point to both teams' qualifier points
            team1.setQualifierPoints(team1.getQualifierPoints() + 1);
            team2.setQualifierPoints(team2.getQualifierPoints() + 1);
        }

        // Update the match object with the result (scores for both teams)
        setResult(team1Score, team2Score);
    }

    public Team getTeam(String abbv) {
        return matchTeams.get(abbv);
    }

    public HashMap<String, Team> getTeamsInMatch() {
        return matchTeams;
    }

}
