package Backend;

import java.util.Collection;

public abstract class Stage {
    private Collection<Team> teams;
    private Collection<Match> matches;
    public Stage(Collection<Team> teams){
        this.teams = teams;
    }
    public abstract void arrangeMatches();
    public void calculateMatchResults(){

    }

    public Collection<Team> getTeams() {
        return teams;
    }
}
