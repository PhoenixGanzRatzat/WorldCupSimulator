package Backend.stage;

import Backend.Match;
import Backend.MatchType;
import Backend.Team;

import java.util.List;

public abstract class Stage {
    private List<Team> teams;
    private List<Match> matches;
    public Stage(List<Team> teams){
        this.teams = teams;
    }
    public abstract void arrangeMatches();
    public void calculateMatchResults(){
        for(Match m:matches){
            m.simulateMatchResult(MatchType.NO_MATCH_TYPE);
        }
    }
    public List<Team> getTeams(){
        return teams;
    }
    public List<Match> getMatches(){
        return matches;
    }
}
