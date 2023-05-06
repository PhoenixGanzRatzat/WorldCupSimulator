package Backend.stage;

import Backend.Match;
import Backend.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class Stage {
    private List<Team> teams;
    private List<Match> matches;
    public Stage(List<Team> teams){
        this.teams = teams;
        matches = new ArrayList<>();
    }
    public abstract void arrangeMatches();
    public void calculateMatchResults(){
        for(Match m:matches){
            m.simulateMatchResult();
        }
    }
    public List<Team> getTeams(){
        return teams;
    }
    public List<Match> getMatches(){
        return matches;
    }
}
