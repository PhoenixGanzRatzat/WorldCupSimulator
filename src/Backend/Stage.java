package Backend;

import java.util.Collection;

public abstract class Stage {

   protected ArrayList<Team> teams;
    protected ArrayList<Match> matches;
   

    public Stage(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Collection<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public abstract void arrangeMatches();

    public void calculateMatchResults() {

    }
}
