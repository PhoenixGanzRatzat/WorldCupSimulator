package Backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Stage {
    private Collection<Team> teams;
    private Collection<Match> matches;
    public Stage(Collection<Team> teams){
        this.teams = teams;
    }
    public abstract void arrangeMatches();
    public void calculateMatchResults(){

    }
}
