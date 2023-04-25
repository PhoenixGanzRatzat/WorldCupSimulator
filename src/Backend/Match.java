package Backend;

import java.util.HashMap;

public class Match {
    private Team team1;
    private Team team2;
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

    public Team getTeam(String abbv) {
        return matchTeams.get(abbv);
    }

    public HashMap<String, Team> getTeamsInMatch() {
        return matchTeams;
    }

}
