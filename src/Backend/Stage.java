package Backend;

import java.util.Collection;

public abstract class Stage {
    protected ArrayList<Team> teams;
    protected ArrayList<Match> matches;
    public Stage(ArrayList<Team> teams){
        this.teams = teams;
    }
    public abstract void arrangeMatches();
    public void calculateMatchResults(){

    }
}
