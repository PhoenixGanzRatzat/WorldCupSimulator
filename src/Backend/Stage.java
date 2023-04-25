package Backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Stage {
    private Collection<Team> teams;
    private Collection<Match> matches;
    public Stage(Collection<Team> teams){
        this.teams = teams;
    }
    public void arrangeMatches(){
        Collections.sort((List<Team>) teams);

    }
    public void calculateMatchResults(){

    }
}
