package Backend;

import java.util.*;

public class KnockoutStage extends Stage {

    private Set<Match> matchesForRoundOfSixteen;
    private Set<Match> matchesForQuarterfinals;
    private Set<Match> matchesForSemifinals;
    private Match finalMatch;

    public KnockoutStage(ArrayList<Team> teams) {
        super(teams);
        matchesForRoundOfSixteen = createMatchesFromTeams(teams);
        matchesForQuarterfinals = new HashSet<>();
        matchesForSemifinals = new HashSet<>();
    }

    @Override
    public void arrangeMatches() {

    }

    private Set<Match> createMatchesFromTeams(ArrayList<Team> teams) {
        // TODO: 4/29/2023 Impl.
        Spliterator<Team> teamSpliterator = teams.spliterator().trySplit();
        return null;
    }

    private ArrayList<Team> simulateRoundOfSixteen(ArrayList<Team> teams) {
        // TODO: 4/28/2023 Impl.
        return null;
    }

    private ArrayList<Team> simulateQuarterfinals(ArrayList<Team> roundOfSixteenWinners) {
        // TODO: 4/28/2023 Impl.
        return null;
    }
    private ArrayList<Team> simulateSemifinals(ArrayList<Team> quarterfinalsWinners) {
        // TODO: 4/29/2023 Impl.
        return null;
    }

    public static void main(String[] args) {
        Team teamOne = new Team("TeamOne", "T1", Region.AFC, 0);
        Team teamTwo = new Team("TeamTwo", "T2", Region.AFC, 0);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(teamOne);
        teams.add(teamTwo);
        KnockoutStage knockoutStage = new KnockoutStage(teams);
    }
}
