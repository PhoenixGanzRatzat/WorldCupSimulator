package Backend;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KnockoutStage extends Stage {

    private Set<Match> matchesForRoundOfSixteen;
    private Set<Match> matchesForQuarterfinals;
    private Set<Match> matchesForSemifinals;
    private Match finalMatch;

    public KnockoutStage(Collection<Team> teams) {
        super(teams);
        matchesForRoundOfSixteen = createMatchesFromTeams(teams);
        matchesForQuarterfinals = new HashSet<>();
        matchesForSemifinals = new HashSet<>();
    }

    @Override
    public void arrangeMatches() {

    }

    private Set<Match> createMatchesFromTeams(Collection<Team> teams) {
        // TODO: 4/29/2023 Impl.
        return null;
    }

    private Collection<Team> simulateRoundOfSixteen(Collection<Team> teams) {
        // TODO: 4/28/2023 Impl.
        return null;
    }

    private Collection<Team> simulateQuarterfinals(Collection<Team> roundOf16Winners) {
        // TODO: 4/28/2023 Impl.
        return null;
    }
    private Collection<Team> simulateSemifinals(Collection<Team> quarterfinalsWinners) {
        // TODO: 4/29/2023 Impl.
        return null;
    }
}