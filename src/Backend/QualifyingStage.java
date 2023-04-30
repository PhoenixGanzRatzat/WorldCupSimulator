package Backend;

import java.util.*;
import java.util.stream.Collectors;

public class QualifyingStage extends Stage {

    public QualifyingStage(ArrayList<Team> teams) {
        super(teams);
    }

    //should populate the matches from each round into the matches collection
    @Override
    public void arrangeMatches() {

        // First round
        List<Match> firstRoundMatches = firstRoundAFC();
        List<Team> firstRoundWinners = firstRoundMatches.stream()
                .map(Match::getWinner)
                .collect(Collectors.toList());

        // Second round
        RoundResult secondRoundResult = secondRoundAFC(firstRoundWinners);
        List<Match> secondRoundMatches = secondRoundResult.getRoundMatches();
        List<Team> qualifiedTeamsFromSecondRound = secondRoundResult.getRoundTeams();


        // Third round
        RoundResult thirdRoundResult = thirdRoundAFC(qualifiedTeamsFromSecondRound);
        List<Match> thirdRoundMatches = thirdRoundResult.getRoundMatches();
        List<Team> thirdPlacedTeams = thirdRoundResult.getRoundTeams();

        // Forth round
        List<Match> fourthRoundMatches = fourthRoundAFC(thirdPlacedTeams);

        // Add all matches to the matches collection
        getMatches().addAll(firstRoundMatches);
        getMatches().addAll(secondRoundMatches);
        getMatches().addAll(thirdRoundMatches);
        getMatches().addAll(fourthRoundMatches);
    }

    private List<List<Team>> createGroups(List<Team> teams, int numberOfGroups, int groupSize) {
        List<List<Team>> groups = new ArrayList<>();

        for (int i = 0; i < numberOfGroups; i++) {
            List<Team> group = new ArrayList<>();
            for (int j = 0; j < groupSize; j++) {
                int index = i * groupSize + j;
                group.add(teams.get(index));
            }
            groups.add(group);
        }

        return groups;
    }

    private List<Match> arrangeRoundRobinMatches(List<Team> teams) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                matches.add(new Match(teams.get(i), teams.get(j)));
                matches.add(new Match(teams.get(j), teams.get(i)));
            }
        }
        return matches;
    }


    public List<Match> firstRoundAFC() {
        // Filter teams with a rank between 35 and 46 (inclusive)
        List<Team> afcTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.AFC.getName()))
                .collect(Collectors.toList());
        Collections.sort(afcTeams);

        List<Team> firstRoundTeams = afcTeams.subList(afcTeams.size() - 13, afcTeams.size() - 1);
        Collections.shuffle(firstRoundTeams);

        return arrangeRoundRobinMatches(firstRoundTeams);
    }


    public RoundResult secondRoundAFC(List<Team> firstRoundWinners) {
        List<Team> afcTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.AFC.getName()))
                .collect(Collectors.toList());

        // Add first round winners to the list of teams participating in the second round
        afcTeams.addAll(firstRoundWinners);

        // Shuffle the list to randomize the teams before dividing into groups
        Collections.shuffle(afcTeams);

        // Divide the teams into eight groups of five teams
        List<List<Team>> groups = createGroups(afcTeams, 8, 5);
        List<Match> secondRoundMatches = new ArrayList<>();
        List<Team> groupWinners = new ArrayList<>();
        List<Team> groupRunnersUp = new ArrayList<>();

        for (List<Team> group : groups) {
            secondRoundMatches.addAll(arrangeRoundRobinMatches(group));

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

            // Add the group winner and runner-up to the respective lists
            groupWinners.add(group.get(0));
            groupRunnersUp.add(group.get(1));
        }

        // Sort the runners-up by their qualifier points in descending order
        groupRunnersUp.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

        // Get the four best group runners-up
        List<Team> bestGroupRunnersUp = groupRunnersUp.subList(0, 4);

        // Combine the winners and the best runners-up
        List<Team> qualifiedTeams = new ArrayList<>(groupWinners);
        qualifiedTeams.addAll(bestGroupRunnersUp);

        return new RoundResult(qualifiedTeams, secondRoundMatches);
    }



    private RoundResult thirdRoundAFC(List<Team> secondRoundWinners) {
        List<List<Team>> thirdRoundGroups = createGroups(secondRoundWinners, 2, 6);
        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Team> thirdPlacedTeams = new ArrayList<>();

        for (List<Team> group : thirdRoundGroups) {
            thirdRoundMatches.addAll(arrangeRoundRobinMatches(group));
        }

        for (List<Team> group : thirdRoundGroups) {
            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

            // Add the top two teams from each group to the qualified teams
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));

            // Add the third-placed team to the thirdPlacedTeams list
            thirdPlacedTeams.add(group.get(2));
        }

        // return a ThirdRoundResult object with the third round matches and the third-placed teams
        return new RoundResult(thirdPlacedTeams, thirdRoundMatches);
    }



    public List<Match> fourthRoundAFC(List<Team> thirdPlacedTeams) {
        // Two third-placed teams from the third round groups
        Team team1 = thirdPlacedTeams.get(0);
        Team team2 = thirdPlacedTeams.get(1);

        // Create a home-and-away match between the two teams
        Match homeMatch = new Match(team1, team2);
        Match awayMatch = new Match(team2, team1);

        // Simulate the match results
        homeMatch.simulateMatchResult();
        awayMatch.simulateMatchResult();

        List<Match> fourthRoundMatches = new ArrayList<>();
        fourthRoundMatches.add(homeMatch);
        fourthRoundMatches.add(awayMatch);

        // Return the fourth round matches
        return fourthRoundMatches;
    }

    //Class to retrieve the teams and matches from a round
    public static class RoundResult {
        private List<Team> roundTeams;
        private List<Match> roundMatches;

        public RoundResult(List<Team> roundTeams, List<Match> roundMatches) {
            this.roundTeams = roundTeams;
            this.roundMatches = roundMatches;
        }

        public List<Team> getRoundTeams() {
            return roundTeams;
        }

        public List<Match> getRoundMatches() {
            return roundMatches;
        }
    }

    //To add other confederation qualification methods here, use the same approach
    // For example:
    // public void firstRoundCAF() { ... }
    // public void firstRoundCONCACAF() { ... }
    // ...
}




