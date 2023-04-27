package Backend;

import java.util.HashMap;

public class Match {
    private Team team1;
    private Team team2;
    private HashMap<String, Team>  matchTeams;
    private int team1Score;
    private int team2Score;

    public Match(){
        this.matchTeams = new HashMap<>();
    }
    public Match(Team team1, Team team2, int score1, int score2){
        this();
        this.team1 = team1;
        this.team2 = team2;
       addTeam(team1);
       addTeam(team2);
       team1Score = score1;
       team2Score = score2;
    }

    public void addTeam(Team team) {
        if (matchTeams.size() < 2) {
            matchTeams.put(team.getAbbv(), team);
        } else {
            System.out.println("Error: Each match can only have two teams.");
        }
    }

    public Team getTeam(String abbv) {
        return matchTeams.get(abbv);
    }

    public HashMap<String, Team> getTeamsInMatch() {
        return matchTeams;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }
}
