package Backend;

import java.util.Collection;

public abstract class Stage {
    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    public Stage(ArrayList<Team> teams){
        this.teams = teams;
    }
    public abstract void arrangeMatches();
    public void calculateMatchResults(){
        for(Match m:matches){
            m.simulateMatchResult();
        }
    }
    public ArrayList<Team> getTeams(){
        return teams;
    }
    public ArrayList<Match> getMatches(){
        return matches;
    }

    public Collection<Team> getTeams() {
        return teams;
    }
}
