package Backend;

import java.util.Collection;

public abstract class Stage {
    private Collection<Team> teams;
    private Collection<Match> matches;

    public Stage(Collection<Team> teams) {
        this.teams = teams;
    }

    public Collection<Team> getTeams() {
        return teams;
    }

    public void setTeams(Collection<Team> teams) {
        this.teams = teams;
    }

    public Collection<Match> getMatches() {
        return matches;
    }

    public void setMatches(Collection<Match> matches) {
        this.matches = matches;
    }

    public abstract void arrangeMatches();

    public void calculateMatchResults() {

    }
}
