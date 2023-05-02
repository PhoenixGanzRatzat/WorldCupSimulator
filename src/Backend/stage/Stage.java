package Backend.stage;

import Backend.Match;
import Backend.MatchType;
import Backend.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class Stage {
    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    public Stage(ArrayList<Team> teams){
        this.teams = teams;
    }
    public abstract void arrangeMatches();
    public void calculateMatchResults(){
        for(Match m:matches){
            m.simulateMatchResult(MatchType.NO_MATCH_TYPE);
        }
    }
    public ArrayList<Team> getTeams(){
        return teams;
    }
    public ArrayList<Match> getMatches(){
        return matches;
    }
}
