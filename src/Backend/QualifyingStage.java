package Backend;

import java.util.*;

public class QualifyingStage extends Stage {
    public QualifyingStage(Collection<Team> teams) {
        super(teams);
    }

    @Override
    public void arrangeMatches() {
        Collection<Match> matches = new ArrayList<>();

        // Group teams by their region
        Map<Region, List<Team>> teamsByRegion = new HashMap<>();
        for (Team team : getTeams()) {
            teamsByRegion.putIfAbsent(team.getRegion(), new ArrayList<>());
            teamsByRegion.get(team.getRegion()).add(team);
        }

        // Arrange matches within each region
        for (Map.Entry<Region, List<Team>> entry : teamsByRegion.entrySet()) {
            List<Team> regionalTeams = entry.getValue();

            // Shuffle the teams in each region to randomize the match arrangement
            Collections.shuffle(regionalTeams);

            // Pair teams within the region for matches
            for (int i = 0; i < regionalTeams.size(); i += 2) {
                Team team1 = regionalTeams.get(i);
                Team team2 = regionalTeams.get(i + 1);
                Match match = new Match(team1, team2);
                matches.add(match);
            }
        }

        setMatches(matches);
    }

    @Override
    public void calculateMatchResults() {
        // Iterate through all the matches in the qualifying stage
        for (Match match : getMatches()) {
            // Simulate the match result by calling the simulateMatchResult() method on the match object
            match.simulateMatchResult();
        }
    }

}
