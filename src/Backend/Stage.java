package Backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
