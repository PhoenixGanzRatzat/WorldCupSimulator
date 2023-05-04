package Backend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class QualifyingStage extends Stage {
    List<Team> worldCupQualifiers = new ArrayList<>();

    public QualifyingStage(ArrayList<Team> teams) {
        super(teams);
    }

    //should populate the matches from each round into the matches collection
    @Override
    public void arrangeMatches() throws InvocationTargetException, IllegalAccessException {

        List<Team> interConfederationPlayoffTeams = new ArrayList<>();

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

            List<Team> winners;
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
            worldCupQualifiers.addAll(winners);

            RoundResult playOffResult = (RoundResult) methods.get(methods.size() - 1).invoke(this, winners);
            matches.addAll(playOffResult.getRoundMatches());
            interConfederationPlayoffTeams.addAll(playOffResult.getPlayOffTeams());


            getMatches().addAll(matches);
        }
        List<Team> interConfederationQualifiers = playInterConfederationPlayoffs(interConfederationPlayoffTeams);
        worldCupQualifiers.addAll(interConfederationQualifiers);


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

    /**
     * This method arranges matches between teams, either as single matches or home-and-away matches.
     *
     * @param teams The list of teams that will play against each other.
     * @param homeAndAway A boolean flag indicating whether the matches should be arranged as home-and-away.
     *                    If set to true, each pair of teams will play two matches, one home and one away.
     *                    If set to false, each pair will play only one match.
     * @return A list of Match objects representing the arranged matches.
     */
    private List<Match> arrangeHomeAndAwayMatches(List<Team> teams, boolean homeAndAway) {
        List<Match> matches = new ArrayList<>();

        // Iterate through each pair of teams in the list
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                // Add a match between the current pair of teams
                matches.add(new Match(teams.get(i), teams.get(j)));

                // If homeAndAway is true, add a reverse match with the teams switched
                if (homeAndAway) {
                    matches.add(new Match(teams.get(j), teams.get(i)));
                }
            }
        }

        return matches;
    }



    public List<Match> firstRoundAFC() {
        // Filter teams belonging to the AFC confederation
        List<Team> afcTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.AFC.getName()))
                .collect(Collectors.toList());

        // Sort the teams according to their ranks
        Collections.sort(afcTeams);

        // Select the teams with ranks between 35 and 46 (inclusive) for the first round
        List<Team> firstRoundTeams = afcTeams.subList(afcTeams.size() - 13, afcTeams.size() - 1);

        // Randomly shuffle the selected teams to create fixtures
        Collections.shuffle(firstRoundTeams);

        // Arrange home-and-away matches for the first round fixtures
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



    public RoundResult fourthRoundAFC(List<Team> thirdPlacedTeams) {
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

        // Determine the winner of the play-off
        Team playOffWinner = determinePlayOffWinner(fourthRoundMatches);

        // Add the loser of the play-off to the list of playoff teams
        Team playOffLoser = Arrays.asList(team1, team2).stream().filter(t -> !t.equals(playOffWinner)).findFirst().orElse(null);
        List<Team> playOffTeams = Collections.singletonList(playOffLoser);

        return new RoundResult(Collections.singletonList(playOffWinner), playOffTeams, fourthRoundMatches);
    }

    //Class to retrieve the teams and matches from a round
    public class RoundResult {
        private List<Team> roundTeams;
        private List<Team> playoffTeams;
        private List<Match> roundMatches;

        public RoundResult(List<Team> winners, List<Team> playoffTeams, List<Match> matches) {
            this.roundTeams = winners;
            this.playoffTeams = playoffTeams;
            this.roundMatches = matches;
        }
        public RoundResult(List<Team> winners, List<Match> matches) {
            this.roundTeams = winners;
            this.roundMatches = matches;
        }

        // Add getters and setters for the new playoffTeams field

        public List<Team> getPlayOffTeams() {
            return playoffTeams;
        }

        public void setPlayoffTeams(List<Team> playoffTeams) {
            this.playoffTeams = playoffTeams;
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

        return new RoundResult(worldCupQualifiers, Collections.singletonList(fourthPlacedTeam), fifthRoundMatches);
    }

    public RoundResult conmebolQualificationRound() {
        List<Team> conmebolTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.CONMEBOL.getName()))
                .collect(Collectors.toList());

        List<Match> conmebolMatches = arrangeHomeAndAwayMatches(conmebolTeams, true);

        // Simulate the matches
        for (Match match : conmebolMatches) {
            match.simulateMatchResult();
        }

        // Sort the teams by their qualifier points in descending order
        conmebolTeams.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

        // Get the top 4 teams who qualify for the FIFA World Cup
        List<Team> worldCupQualifiers = conmebolTeams.subList(0, 4);

        // Get the fifth-placed team who advances to the inter-confederation play-offs
        Team fifthPlacedTeam = conmebolTeams.get(4);

        return new RoundResult(worldCupQualifiers, Collections.singletonList(fifthPlacedTeam), conmebolMatches);
    }

    public List<Match> firstRoundOFC() {
        List<Team> ofcFirstRoundTeams = getTeams().stream()
                .filter(team -> team.getRegion().equals(Region.OFC.getName()))
                .filter(team -> team.getRank() <= 4)
                .collect(Collectors.toList());

        return arrangeHomeAndAwayMatches(ofcFirstRoundTeams, true);
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
        // Divide the six teams into two groups of three teams
        List<List<Team>> groups = createGroups(secondRoundWinners, 2, 3);
        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Team> groupWinners = new ArrayList<>();

        for (List<Team> group : groups) {
            // Arrange home-and-away round-robin matches within each group
            thirdRoundMatches.addAll(arrangeHomeAndAwayMatches(group, true));

            // Simulate the matches and calculate the points
            for (Match match : thirdRoundMatches) {
                match.simulateMatchResult();
            }

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getQualifierPoints(), t1.getQualifierPoints()));

            // Add the group winner to the list of group winners
            groupWinners.add(group.get(0));
        }

        // Play the two-legged match between the group winners
        List<Match> playOffMatches = arrangeHomeAndAwayMatches(groupWinners, false);

        // Determine the winner of the play-off
        Team playOffWinner = determinePlayOffWinner(playOffMatches);

        // Add the loser of the play-off to the list of playoff teams
        Team playOffLoser = groupWinners.stream().filter(t -> !t.equals(playOffWinner)).findFirst().orElse(null);
        List<Team> playOffTeams = Collections.singletonList(playOffLoser);

        return new RoundResult(Collections.singletonList(playOffWinner), playOffTeams, thirdRoundMatches);
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

    private Team determinePlayOffWinner(List<Match> playOffMatches) {
        int homeTeamGoals = playOffMatches.get(0).getTeam1Score() + playOffMatches.get(1).getTeam2Score();
        int awayTeamGoals = playOffMatches.get(0).getTeam2Score() + playOffMatches.get(1).getTeam1Score();

        // Get the home and away teams
        Team homeTeam = playOffMatches.get(0).getTeam(playOffMatches.get(0).getTeamsInMatch().keySet().toArray(new String[0])[0]);
        Team awayTeam = playOffMatches.get(0).getTeam(playOffMatches.get(0).getTeamsInMatch().keySet().toArray(new String[0])[1]);

        // If one team scored more goals, they are the winner
        if (homeTeamGoals > awayTeamGoals) {
            return homeTeam;
        } else if (awayTeamGoals > homeTeamGoals) {
            return awayTeam;
        }

        // If both teams scored the same number of goals, use the away goals rule
        int homeTeamAwayGoals = playOffMatches.get(1).getTeam2Score();
        int awayTeamAwayGoals = playOffMatches.get(0).getTeam2Score();

        if (homeTeamAwayGoals > awayTeamAwayGoals) {
            return homeTeam;
        } else if (awayTeamAwayGoals > homeTeamAwayGoals) {
            return awayTeam;
        }

        // If the teams are still tied, use a penalty shootout to determine the winner
        // (This is a simplified example, you can implement a more realistic penalty shootout if you prefer)
        return Math.random() > 0.5 ? homeTeam : awayTeam;
    }

    public Team playPlayoffMatch(Team team1, Team team2) {
        Match homeMatch = new Match(team1, team2);
        Match awayMatch = new Match(team2, team1);

        homeMatch.simulateMatchResult();
        awayMatch.simulateMatchResult();

        // Determine the winner using the determinePlayOffWinner method
        List<Match> playOffMatches = Arrays.asList(homeMatch, awayMatch);
        return determinePlayOffWinner(playOffMatches);
    }


    public List<Team> playInterConfederationPlayoffs(List<Team> playoffTeams) {
        List<Team> worldCupQualifiers = new ArrayList<>();

        // Simulate play-offs between the confederations (AFC vs CONCACAF and CONMEBOL vs OFC)
        Team afcVsConcacafWinner = playPlayoffMatch(playoffTeams.get(0), playoffTeams.get(1));
        Team conmebolVsOfcWinner = playPlayoffMatch(playoffTeams.get(2), playoffTeams.get(3));

        // Add the winners to the list of World Cup qualifiers
        worldCupQualifiers.add(afcVsConcacafWinner);
        worldCupQualifiers.add(conmebolVsOfcWinner);

        return worldCupQualifiers;
    }
}




