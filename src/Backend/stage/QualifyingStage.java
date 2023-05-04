package Backend.stage;

import Backend.Match;
import Backend.Region;
import Backend.Team;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class QualifyingStage extends Stage {

    public QualifyingStage(List<Team> teams) {
        super(teams);
    }

    //should populate the matches from each round into the matches collection
    @Override
    public void arrangeMatches() {
        Map<String, List<Method>> confederationMethods = new HashMap<>();

        try {
            // Add AFC methods to the map
            confederationMethods.put(Region.AFC.getName(), Arrays.asList(
                    getClass().getDeclaredMethod("firstRoundAFC"),
                    getClass().getDeclaredMethod("secondRoundAFC", List.class),
                    getClass().getDeclaredMethod("thirdRoundAFC", List.class),
                    getClass().getDeclaredMethod("fourthRoundAFC", List.class)
            ));

            // Add CAF methods to the map
            confederationMethods.put(Region.CAF.getName(), Arrays.asList(
                    getClass().getDeclaredMethod("firstRoundCAF"),
                    getClass().getDeclaredMethod("secondRoundCAF", List.class),
                    getClass().getDeclaredMethod("thirdRoundCAF", List.class)
            ));

            // Add CONCACAF methods to the map
            confederationMethods.put(Region.CONCACAF.getName(), Arrays.asList(
                    getClass().getDeclaredMethod("firstRoundCONCACAF"),
                    getClass().getDeclaredMethod("secondRoundCONCACAF", List.class),
                    getClass().getDeclaredMethod("thirdRoundCONCACAF", List.class),
                    getClass().getDeclaredMethod("fourthRoundCONCACAF", List.class),
                    getClass().getDeclaredMethod("fifthRoundCONCACAF", List.class)
            ));

            confederationMethods.put(Region.CONMEBOL.getName(), Arrays.asList(
                    getClass().getDeclaredMethod("conmebolQualificationRound")

            ));

            confederationMethods.put(Region.OFC.getName(), Arrays.asList(
                    getClass().getDeclaredMethod("firstRoundOFC"),
                    getClass().getDeclaredMethod("secondRoundOFC", List.class),
                    getClass().getDeclaredMethod("thirdRoundOFC", List.class)

            ));
            confederationMethods.put(Region.UEFA.getName(), Arrays.asList(
                    getClass().getDeclaredMethod("firstRoundUEFA"),
                    getClass().getDeclaredMethod("secondRoundUEFA", List.class)
            ));

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error initializing confederationMethods", e);
        }

        for (String confederation : confederationMethods.keySet()) {
            List<Method> methods = confederationMethods.get(confederation);

            List<Team> winners = new ArrayList<>();
            List<Match> matches = new ArrayList<>();

            try {
                // First, invoke the first method (the first round) for the current confederation
                matches.addAll((List<Match>) methods.get(0).invoke(this));
                winners = matches.stream()
                        .map(Match::getWinner)
                        .collect(Collectors.toList());

                // Then, iterate through the rest of the methods (second, third, and other rounds) for the current confederation
                for (int i = 1; i < methods.size(); i++) {
                    RoundResult roundResult = (RoundResult) methods.get(i).invoke(this, winners);
                    matches.addAll(roundResult.getRoundMatches());
                    winners = roundResult.getRoundTeams();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error invoking arrangeMatches methods", e);
            }

            getMatches().addAll(matches);
        }


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

    private List<Match> arrangeHomeAndAwayMatches(List<Team> teams, boolean homeAndAway) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                matches.add(new Match(teams.get(i), teams.get(j)));
                if (homeAndAway) {
                    matches.add(new Match(teams.get(j), teams.get(i)));
                }
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

        return arrangeHomeAndAwayMatches(firstRoundTeams, true);
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
            secondRoundMatches.addAll(arrangeHomeAndAwayMatches(group, true));

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
            thirdRoundMatches.addAll(arrangeHomeAndAwayMatches(group, true));
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

    public List<Match> firstRoundCAF() {
        // Filter teams with a rank between 35 and 46 (inclusive)
        List<Team> cafTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CAF.getName()))
                .collect(Collectors.toList());
        Collections.sort(cafTeams);

        List<Team> firstRoundTeams = cafTeams.subList(cafTeams.size() - 26, cafTeams.size() - 1);
        Collections.shuffle(firstRoundTeams);

        return arrangeHomeAndAwayMatches(firstRoundTeams, true);
    }

    public RoundResult secondRoundCAF(List<Team> firstRoundWinners) {
        List<Team> cafTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CAF.getName()))
                .collect(Collectors.toList());

        // Add first round winners to the list of teams participating in the second round
        cafTeams.addAll(firstRoundWinners);

        // Shuffle the list to randomize the teams before arranging matches
        Collections.shuffle(cafTeams);

        List<Match> secondRoundMatches = new ArrayList<>();

        // Arrange matches in pairs
        for (int i = 0; i < cafTeams.size(); i += 2) {
            secondRoundMatches.add(new Match(cafTeams.get(i), cafTeams.get(i + 1)));
            secondRoundMatches.add(new Match(cafTeams.get(i + 1), cafTeams.get(i)));
        }

        return new RoundResult(cafTeams, secondRoundMatches);
    }


    public RoundResult thirdRoundCAF(List<Team> secondRoundWinners) {
        List<List<Team>> groups = createGroups(secondRoundWinners, 5, 4);
        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Team> thirdRoundTeams = new ArrayList<>(secondRoundWinners);

        for (List<Team> group : groups) {
            thirdRoundMatches.addAll(arrangeHomeAndAwayMatches(group, false));
        }


        for (List<Team> group : groups) {
            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

            // Add the top team from each group to the qualified teams
            thirdRoundTeams.add(group.get(0));
        }

        return new RoundResult(thirdRoundTeams, thirdRoundMatches);
    }

    public RoundResult firstRoundCONCACAF() {
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CONCACAF.getName()))
                .collect(Collectors.toList());
        Collections.sort(concacafTeams);

        List<Team> firstRoundTeams = concacafTeams.subList(21, 35);
        List<Match> firstRoundMatches = arrangeHomeAndAwayMatches(firstRoundTeams, true);
        List<Team> firstRoundWinners = firstRoundMatches.stream()
                .map(Match::getWinner)
                .collect(Collectors.toList());

        return new RoundResult(firstRoundWinners, firstRoundMatches);
    }

    public RoundResult secondRoundCONCACAF(List<Team> firstRoundWinners) {
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CONCACAF.getName()))
                .collect(Collectors.toList());
        List<Team> secondRoundTeams = new ArrayList<>(concacafTeams.subList(8, 21));
        secondRoundTeams.addAll(firstRoundWinners);

        List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(secondRoundTeams, true);
        List<Team> secondRoundWinners = secondRoundMatches.stream()
                .map(Match::getWinner)
                .collect(Collectors.toList());

        return new RoundResult(secondRoundWinners, secondRoundMatches);
    }

    public RoundResult thirdRoundCONCACAF(List<Team> secondRoundWinners) {
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CONCACAF.getName()))
                .collect(Collectors.toList());
        List<Team> thirdRoundTeams = new ArrayList<>(concacafTeams.subList(6, 8));
        thirdRoundTeams.addAll(secondRoundWinners);

        List<Match> thirdRoundMatches = arrangeHomeAndAwayMatches(thirdRoundTeams, true);
        List<Team> thirdRoundWinners = thirdRoundMatches.stream()
                .map(Match::getWinner)
                .collect(Collectors.toList());

        return new RoundResult(thirdRoundWinners, thirdRoundMatches);
    }

    public RoundResult fourthRoundCONCACAF(List<Team> thirdRoundWinners) {
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CONCACAF.getName()))
                .collect(Collectors.toList());
        Collections.sort(concacafTeams);

        List<Team> fourthRoundTeams = concacafTeams.subList(0, 6);
        fourthRoundTeams.addAll(thirdRoundWinners);
        List<List<Team>> groups = createGroups(fourthRoundTeams, 3, 4);

        List<Match> fourthRoundMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();

        for (List<Team> group : groups) {
            fourthRoundMatches.addAll(arrangeHomeAndAwayMatches(group, false));
        }

        for (List<Team> group : groups) {
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
        }

        return new RoundResult(qualifiedTeams, fourthRoundMatches);
    }

    public RoundResult fifthRoundCONCACAF(List<Team> fourthRoundWinners) {
        List<Match> fifthRoundMatches = arrangeHomeAndAwayMatches(fourthRoundWinners, true);
        List<Team> qualifiedTeams = new ArrayList<>(fourthRoundWinners);

        // Sort the teams by their qualifier points in descending order
        qualifiedTeams.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

        // Get the top 3 teams who qualify for the FIFA World Cup
        List<Team> worldCupQualifiers = qualifiedTeams.subList(0, 3);

        // Get the fourth-placed team who advances to the inter-confederation play-offs
        Team fourthPlacedTeam = qualifiedTeams.get(3);

        // You can create a custom result class or return the result in any other way you prefer
        return new RoundResult(worldCupQualifiers, fifthRoundMatches);
    }

    public List<Match> conmebolQualificationRound() {
        List<Team> conmebolTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CONMEBOL.getName()))
                .collect(Collectors.toList());

        return arrangeHomeAndAwayMatches(conmebolTeams, true);
    }

    public List<Match> firstRoundOFC() {
        List<Team> ofcFirstRoundTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.OFC.getName()))
                .filter(team -> team.getRank() <= 4)
                .collect(Collectors.toList());

        return arrangeHomeAndAwayMatches(ofcFirstRoundTeams, true);
    }

    @Override
    public void calculateMatchResults() {
        // Iterate through all the matches in the qualifying stage
        for (Match match : getMatches()) {
            // Simulate the match result by calling the simulateMatchResult() method on the match object
            match.simulateMatchResult();
        }
    }
    public RoundResult secondRoundOFC(Team firstRoundWinner) {
        List<Team> ofcSecondRoundTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.OFC.getName()))
                .filter(team -> team.getRank() > 4)
                .collect(Collectors.toList());

        ofcSecondRoundTeams.add(firstRoundWinner);
        List<List<Team>> groups = createGroups(ofcSecondRoundTeams, 2, 4);
        List<Match> secondRoundMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();

        for (List<Team> group : groups) {
            secondRoundMatches.addAll(arrangeHomeAndAwayMatches(group, true));

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

            // Add the top three teams from each group to the qualified teams
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
            qualifiedTeams.add(group.get(2));
        }
        return new RoundResult(qualifiedTeams, secondRoundMatches);
    }
    public RoundResult thirdRoundOFC(List<Team> secondRoundWinners) {
        List<List<Team>> groups = createGroups(secondRoundWinners, 2, 3);
        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Team> groupWinners = new ArrayList<>();

        for (List<Team> group : groups) {
            thirdRoundMatches.addAll(arrangeHomeAndAwayMatches(group, true));

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

            // Add the top team from each group to the groupWinners list
            groupWinners.add(group.get(0));
        }

        return new RoundResult(groupWinners, thirdRoundMatches);
    }

    public RoundResult firstRoundUEFA() {
        List<Team> uefaTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.UEFA.getName()))
                .collect(Collectors.toList());

        List<List<Team>> groups = createGroups(uefaTeams, 9, 6);
        List<Match> firstRoundMatches = new ArrayList<>();
        List<Team> groupWinners = new ArrayList<>();

        for (List<Team> group : groups) {
            firstRoundMatches.addAll(arrangeHomeAndAwayMatches(group, true));

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

            // Add the group winner to the groupWinners list
            groupWinners.add(group.get(0));
        }

        return new RoundResult(groupWinners, firstRoundMatches);
    }

    public RoundResult secondRoundUEFA(List<Team> firstRoundRunnersUp) {
        List<List<Team>> playOffPairs = createGroups(firstRoundRunnersUp, 4, 2);
        List<Match> secondRoundMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();

        for (List<Team> pair : playOffPairs) {
            List<Match> playOffMatches = arrangeHomeAndAwayMatches(pair, true);
            secondRoundMatches.addAll(playOffMatches);

            Team winner = playOffMatches.get(0).getWinner();
            qualifiedTeams.add(winner);
    }
        return new RoundResult(qualifiedTeams, secondRoundMatches);
    }
}




