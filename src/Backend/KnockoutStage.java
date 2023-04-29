package Backend;

import java.util.*;

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
        Spliterator<Team> teamSpliterator = teams.spliterator().trySplit();
        return null;
    }

    private Collection<Team> simulateRoundOfSixteen(Collection<Team> teams) {
        // TODO: 4/28/2023 Impl.
        return null;
    }

    private Collection<Team> simulateQuarterfinals(Collection<Team> roundOfSixteenWinners) {
        // TODO: 4/28/2023 Impl.
        return null;
    }
    private Collection<Team> simulateSemifinals(Collection<Team> quarterfinalsWinners) {
        // TODO: 4/29/2023 Impl.
        return null;
    }

    public static void main(String[] args) {
        Team teamOne = new Team("TeamOne", "T1", Region.AFC, 0);
        Team teamTwo = new Team("TeamTwo", "T2", Region.AFC, 0);
        List<Team> teams = Arrays.asList(teamOne, teamTwo);
        KnockoutStage knockoutStage = new KnockoutStage(teams);
    }
}
