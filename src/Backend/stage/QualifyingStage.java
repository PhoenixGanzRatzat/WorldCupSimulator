package Backend.stage;

import Backend.Match;
import Backend.Region;
import Backend.Team;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class QualifyingStage extends Stage {
    List<Match> QualifierMatches;

    // Constructor
    public QualifyingStage(List<Team> teams) {
        super(teams);
        QualifierMatches = new ArrayList<>();
    }

    // Arrange matches for all rounds and regions
    @Override
    public void arrangeMatches() {
        // Arrange matches for each round and region
        firstRoundResultAFC = firstRoundAFC();
        secondRoundResultAFC = secondRoundAFC();
        thirdRoundResultAFC = thirdRoundAFC();
        fourthRoundResultAFC = fourthRoundAFC();
        firstRoundResultCAF = firstRoundCAF();
        secondRoundResultCAF = secondRoundCAF();
        thirdRoundResultCAF = thirdRoundCAF();
        firstRoundResultCONCACAF = firstRoundCONCACAF();
        secondRoundResultCONCACAF = secondRoundCONCACAF();
        thirdRoundResultCONCACAF = thirdRoundCONCACAF();
        fourthRoundResultCONCACAF = fourthRoundCONCACAF();
        fifthRoundResultCONCACAF = fifthRoundCONCACAF();
        firstRoundResultCONMEBOL = roundCONMEBOL();
        firstRoundResultOFC = firstRoundOFC();
        secondRoundResultOFC = secondRoundOFC();
        thirdRoundResultOFC = thirdRoundOFC();
        firstRoundResultUEFA = firstRoundUEFA();
        secondRoundResultUEFA = secondRoundUEFA();
        playoffResult = playInterConfederationPlayoffs();

        // Create a list of RoundResult objects
        List<RoundResult> allRoundResults = Arrays.asList(
                firstRoundResultAFC, secondRoundResultAFC, thirdRoundResultAFC, fourthRoundResultAFC,
                firstRoundResultCAF, secondRoundResultCAF, thirdRoundResultCAF,
                firstRoundResultCONCACAF, secondRoundResultCONCACAF, thirdRoundResultCONCACAF, fourthRoundResultCONCACAF, fifthRoundResultCONCACAF,
                firstRoundResultCONMEBOL,
                firstRoundResultOFC, secondRoundResultOFC, thirdRoundResultOFC,
                firstRoundResultUEFA, secondRoundResultUEFA,
                playoffResult
        );

        // Add all matches to the QualifierMatches list
        addAllMatches(allRoundResults);

    }

    // Add all matches from the roundResults list to the QualifierMatches list
    private void addAllMatches(List<RoundResult> roundResults) {
        for (RoundResult roundResult : roundResults) {
            QualifierMatches.addAll(roundResult.getRoundMatches());
        }
        rearrangeMatchDates(QualifierMatches,  6);
    }

    // Get the list of all qualifier matches
    public List<Match> getMatches() {
        return QualifierMatches;
    }

    // Get the list of all qualified teams
    public ArrayList<Team> qualifiedTeams(){
        Team host = new Team("Russia", "RUS", Region.UEFA, 0);
        ArrayList<Team> qualifiedTeams = new ArrayList<>();
        qualifiedTeams.add(host);
        qualifiedTeams.addAll(thirdRoundResultAFC.getRoundTeams());
        qualifiedTeams.addAll(thirdRoundResultCAF.getRoundTeams());
        qualifiedTeams.addAll(fifthRoundResultCONCACAF.getRoundTeams());
        qualifiedTeams.addAll(firstRoundResultCONMEBOL.getRoundTeams());
        qualifiedTeams.addAll(firstRoundResultUEFA.getRoundTeams());
        qualifiedTeams.addAll(secondRoundResultUEFA.getRoundTeams());
        qualifiedTeams.addAll(playoffResult.getRoundTeams());

        return qualifiedTeams;
    }

    public void assignDatesToRoundMatches(List<Match> matches, LocalDate startDate, int interval, int maxMatchesPerDay) {
        // Maps to store matches per day and team match dates
        Map<LocalDate, Integer> matchesPerDay = new HashMap<>();
        Map<Team, Set<LocalDate>> teamMatchDates = new HashMap<>();

        // List of allowed months
        List<Integer> allowedMonths = Arrays.asList(3, 6, 9, 10, 11);

        // Loop through all matches
        for (Match match : matches) {
            LocalDate matchDate = startDate;
            boolean matchScheduled = false;
            while (!matchScheduled) {
                // Check if the current month is not allowed
                if (!allowedMonths.contains(matchDate.getMonthValue())) {
                    // Find the next allowed month within the same year or in the next year
                    int currentMonth = matchDate.getMonthValue();
                    int nextAllowedMonth = allowedMonths.stream()
                            .filter(allowedMonth -> allowedMonth > currentMonth)
                            .findFirst()
                            .orElse(allowedMonths.get(0));

                    // Update the match date accordingly
                    if (nextAllowedMonth > currentMonth) {
                        matchDate = matchDate.withMonth(nextAllowedMonth).withDayOfMonth(1);
                    } else {
                        matchDate = matchDate.plusYears(1).withMonth(nextAllowedMonth).withDayOfMonth(1);
                    }
                    continue;
                }

                // Check if the match date is beyond November 2017, if so, set the match date back to the start date plus 2 days
                if (matchDate.isAfter(LocalDate.of(2017, 11, 30))) {
                    matchDate = startDate.plusDays(2);
                }

                int matchesOnDate = matchesPerDay.getOrDefault(matchDate, 0);

                Set<LocalDate> team1MatchDates = teamMatchDates.getOrDefault(match.getTeam1(), new HashSet<>());
                Set<LocalDate> team2MatchDates = teamMatchDates.getOrDefault(match.getTeam2(), new HashSet<>());

                LocalDate finalMatchDate = matchDate;
                boolean team1HasMatchOnDate = team1MatchDates.stream().anyMatch(date -> date.equals(finalMatchDate));
                boolean team2HasMatchOnDate = team2MatchDates.stream().anyMatch(date -> date.equals(finalMatchDate));

                /// Check if both teams don't have matches on the current date
                if (matchesOnDate < maxMatchesPerDay && !team1HasMatchOnDate && !team2HasMatchOnDate) {
                    // Set the match date and update the maps
                    match.setMatchDate(matchDate);
                    matchesPerDay.put(matchDate, matchesOnDate + 1);

                    team1MatchDates.add(matchDate);
                    team2MatchDates.add(matchDate);
                    teamMatchDates.put(match.getTeam1(), team1MatchDates);
                    teamMatchDates.put(match.getTeam2(), team2MatchDates);

                    matchScheduled = true;
                } else {
                    // Move to the next date
                    matchDate = matchDate.plusDays(interval);
                }
            }
        }
    }

    public void rearrangeMatchDates(List<Match> matches, int maxMatchesPerDay) {
        // Maps to store matches per day
        Map<LocalDate, Integer> matchesPerDay = new HashMap<>();

        // Loop through all matches
        for (Match match : matches) {
            LocalDate matchDate = match.getMatchDate();
            boolean matchScheduled = false;
            while (!matchScheduled) {
                int matchesOnDate = matchesPerDay.getOrDefault(matchDate, 0);

                // Check if there is room for another match on the current date
                if (matchesOnDate < maxMatchesPerDay) {
                    // Update the maps
                    matchesPerDay.put(matchDate, matchesOnDate + 1);
                    matchScheduled = true;
                } else {
                    // Move to the next date
                    matchDate = matchDate.plusDays(1);
                }
            }
            // Set the match date
            match.setMatchDate(matchDate);
        }
    }


    private RoundResult firstRoundAFC() {
        // Filter the teams for those belonging to the AFC region and ranked 35-46
        List<Team> firstRoundTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Backend.Region.AFC)
                .filter(team -> team.getRank() >= 35 && team.getRank() <= 46)
                .collect(Collectors.toList());


        List<List<Team>> groups = createGroups(firstRoundTeams, firstRoundTeams.size() / 2, 2);
        List<Team> winningTeams = new ArrayList<>();
        List<Match> firstRoundMatches = new ArrayList<>();

        for (List<Team> pair : groups) {
            // Generate home and away matches for each pair
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(pair, true);
            firstRoundMatches.addAll(pairedMatches);
        }

        // Assign dates to the first round matches
        assignDatesToRoundMatches(firstRoundMatches, LocalDate.of(2015, 3, 12), 5, 6);

        // Simulate the matches
        for (Match match : firstRoundMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = firstRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }


        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }



    private RoundResult secondRoundAFC() {
        // Get the top 34 teams and the 6 first round winners
        List<Team> secondRoundTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Backend.Region.AFC)
                .sorted(Comparator.comparing(Team::getRank))
                .limit(34)
                .collect(Collectors.toList());
        // Add the 6 first round winners
        secondRoundTeams.addAll(firstRoundResultAFC.getRoundTeams());

        // Shuffle the list to randomize the teams before dividing into groups
        Collections.shuffle(secondRoundTeams);

        // Divide the teams into eight groups of five teams
        List<List<Team>> groups = createGroups(secondRoundTeams, 8, 5);
        List<Team> groupWinners = new ArrayList<>();
        List<Team> groupRunnersUp = new ArrayList<>();
        List<Match> allGroupMatches = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2015, 6, 11), 3, 4);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the group winner and runner-up to the respective lists
            groupWinners.add(group.get(0));
            groupRunnersUp.add(group.get(1));
        }

        // Get the last match date in the group
        LocalDate lastMatchDate = allGroupMatches.get(allGroupMatches.size() - 1).getMatchDate();

        // Sort the runners-up by their qualifier points in descending order
        groupRunnersUp.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

        // Get the four best group runners-up
        List<Team> bestGroupRunnersUp = groupRunnersUp.subList(0, 4);

        // Combine the winners and the best runners-up
        List<Team> qualifiedTeams = new ArrayList<>(groupWinners);
        qualifiedTeams.addAll(bestGroupRunnersUp);

        return new RoundResult(qualifiedTeams, allGroupMatches);
    }




    private RoundResult thirdRoundAFC() {
        List<List<Team>> thirdRoundGroups = createGroups(secondRoundResultAFC.getRoundTeams(), 2, 6);
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Team> thirdPlacedTeams = new ArrayList<>();
        List<Match> allGroupMatches = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : thirdRoundGroups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }

        // Assign dates to the matches with a 2-day interval and 6 matches per day
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2016, 9, 1), 11, 2);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : thirdRoundGroups) {
            // Get the matches for the current group
            List<Match> thirdRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());
            // Get the last match date in the group
            LocalDate lastMatchDate = thirdRoundMatches.get(thirdRoundMatches.size() - 1).getMatchDate();
            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the top two teams from each group to the qualified teams
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));

            // Add the third-placed team to the thirdPlacedTeams list
            thirdPlacedTeams.add(group.get(2));
        }

        // return a ThirdRoundResult object with the third round matches and the third-placed teams
        return new RoundResult(qualifiedTeams, thirdPlacedTeams, allGroupMatches);
    }

    public RoundResult fourthRoundAFC() {
        // Two third-placed teams from the third round groups
        Team team1 = getThirdRoundResultAFC().getPlayOffTeams().get(0);
        Team team2 = getThirdRoundResultAFC().getPlayOffTeams().get(1);

        // Create home-and-away matches between the two teams
        List<Match> fourthRoundMatches = arrangeHomeAndAwayMatches(Arrays.asList(team1, team2), true);

        // Assign dates to the fourth round matches
        assignDatesToRoundMatches(fourthRoundMatches, LocalDate.of(2017, 10, 5), 5, 1);

        // Simulate the matches
        for (Match match : fourthRoundMatches) {
            match.simulateMatchResult();
        }

        // Determine the winner of the play-off
        Team playOffWinner = determinePlayOffWinner(fourthRoundMatches);

        return new RoundResult(Collections.singletonList(playOffWinner), fourthRoundMatches);
    }

    public RoundResult firstRoundCAF() {
        // Filter teams with a rank between 28 and 53 (inclusive)
        List<Team> cafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Backend.Region.CAF)
                .filter(team -> team.getRank() >= 28 && team.getRank() <= 53)
                .collect(Collectors.toList());

        List<List<Team>> groups = createGroups(cafTeams, cafTeams.size() / 2, 2);
        List<Team> winningTeams = new ArrayList<>();
        List<Match> firstRoundMatches = new ArrayList<>();

        for (List<Team> pair : groups) {
            // Generate home and away matches for each pair
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(pair, true);
            firstRoundMatches.addAll(pairedMatches);
        }

        // Assign dates to the first round matches
        assignDatesToRoundMatches(firstRoundMatches, LocalDate.of(2015, 10, 7), 1, 6);

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = firstRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }


        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }

    public RoundResult secondRoundCAF() {
        List<Team> cafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Backend.Region.CAF)
                .sorted(Comparator.comparing(Team::getRank))
                .limit(27)
                .collect(Collectors.toList());
        // Add the 6 first round winners
        cafTeams.addAll(firstRoundResultCAF.getRoundTeams());

        List<List<Team>> groups = createGroups(cafTeams, cafTeams.size() / 2, 2);
        List<Match> secondRoundMatches = new ArrayList<>();
        List<Team> winningTeams = new ArrayList<>();

        for (List<Team> pair : groups) {
            // Generate home and away matches for each pair
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(pair, true);
            secondRoundMatches.addAll(pairedMatches);
        }

        // Assign dates to the first round matches
        assignDatesToRoundMatches(secondRoundMatches, LocalDate.of(2015, 11, 9), 1, 6);

        // Simulate the matches
        for (Match match : secondRoundMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = secondRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }

        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, secondRoundMatches);
    }

    public RoundResult thirdRoundCAF() {

        // Divide the teams into eight groups of five teams
        List<List<Team>> groups = createGroups(secondRoundResultCAF.getRoundTeams(), 5, 4);
        List<Team> groupWinners = new ArrayList<>();
        List<Match> allGroupMatches = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }
        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2016, 10, 7), 9, 3);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the group winner and runner-up to the respective lists
            groupWinners.add(group.get(0));
        }

        // Combine the winners and the best runners-up
        List<Team> qualifiedTeams = new ArrayList<>(groupWinners);

        return new RoundResult(qualifiedTeams, allGroupMatches);
    }

    private RoundResult processRoundCONCACAF(Region region, int minRank, int maxRank, LocalDate startDate, int daysBetween, int matchPerDay, boolean isFirstRound) {
        // Filter the teams for those belonging to the CONCACAF region and within the specified rank range
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == region)
                .filter(team -> team.getRank() >= minRank && team.getRank() <= maxRank)
                .collect(Collectors.toList());

        if (isFirstRound) {
            concacafTeams.addAll(getFirstRoundResultCONCACAF().getRoundTeams());
        }

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, concacafTeams.size() / 2, 2);

        List<Match> roundMatches = new ArrayList<>();
        List<Team> winningTeams = new ArrayList<>();

        for (List<Team> group : groups) {
            // Generate home and away matches for each group
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(group, true);
            roundMatches.addAll(pairedMatches);
        }

        // Assign dates to the round matches
        assignDatesToRoundMatches(roundMatches, startDate, daysBetween, matchPerDay);

        // Simulate the matches
        for (Match match : roundMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = roundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }

        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, roundMatches);
    }

//    private RoundResult firstRoundCONCACAF() {
//        return processRoundCONCACAF(Region.CONCACAF,22, 35, LocalDate.of(2015, 3, 1), 4, 2, false);
//    }
//
//    private RoundResult secondRoundCONCACAF() {
//        return processRoundCONCACAF(Region.CONCACAF, 9, 21, LocalDate.of(2015, 6, 1), 3, 2, true);
//    }


    private RoundResult firstRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 22-35
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 22 && team.getRank() <= 35)
                .collect(Collectors.toList());

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, concacafTeams.size() / 2, 2);

        List<Match> firstRoundMatches = new ArrayList<>();
        List<Team> winningTeams = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> pairMatches = arrangeHomeAndAwayMatches(group, true);
            firstRoundMatches.addAll(pairMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(firstRoundMatches, LocalDate.of(2015, 3, 1), 4, 2);

        // Simulate the matches
        for (Match match : firstRoundMatches) {
            match.simulateMatchResult();
        }


        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = firstRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }

        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }

    private RoundResult secondRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 22-35
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 9 && team.getRank() <= 21)
                .collect(Collectors.toList());

        concacafTeams.addAll(getFirstRoundResultCONCACAF().getRoundTeams());



        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, concacafTeams.size() / 2, 2);

        List<Match> secondRoundMatches = new ArrayList<>();
        List<Team> winningTeams = new ArrayList<>();

        for (List<Team> pair : groups) {
            // Generate home and away matches for each pair
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(pair, true);
            secondRoundMatches.addAll(pairedMatches);
        }

        // Assign dates to the first round matches
        assignDatesToRoundMatches(secondRoundMatches, LocalDate.of(2015, 6, 1), 3, 2);

        // Simulate the matches
        for (Match match : secondRoundMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = secondRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }

        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, secondRoundMatches);
    }

    private RoundResult thirdRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 7-8
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 7 && team.getRank() <= 8)
                .collect(Collectors.toList());

        concacafTeams.addAll(getSecondRoundResultCONCACAF().getRoundTeams());

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, concacafTeams.size() / 2, 2);

        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Team> winningTeams = new ArrayList<>();

        for (List<Team> pair : groups) {
            // Generate home and away matches for each pair
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(pair, true);
            thirdRoundMatches.addAll(pairedMatches);
        }

        // Assign dates to the first round matches
        assignDatesToRoundMatches(thirdRoundMatches, LocalDate.of(2015, 9, 1), 5, 2);
        // Simulate the matches
        for (Match match : thirdRoundMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = thirdRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }

        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, thirdRoundMatches);
    }

    private RoundResult fourthRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 7-8
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 1 && team.getRank() <= 6)
                .collect(Collectors.toList());

        concacafTeams.addAll(getThirdRoundResultCONCACAF().getRoundTeams());

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, 3, 4);

        List<Match> fourthRoundMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Match> allGroupMatches = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2015, 11, 1), 3, 2);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the group winner and runner-up to the respective lists
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
        }
        // return a FourthRoundResult object with the third round matches
        return new RoundResult(qualifiedTeams, allGroupMatches);
    }

    private RoundResult fifthRoundCONCACAF() {

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(getFourthRoundResultCONCACAF().getRoundTeams(), 1, 6);

        List<Match> fifthRoundMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Team> fourthPlacedTeams = new ArrayList<>();
        List<Match> allGroupMatches = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2016, 11, 2), 10, 2);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the group winner and runner-up to the respective lists
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
            qualifiedTeams.add(group.get(2));

            fourthPlacedTeams.add(group.get(3));


        }

        // return a FourthRoundResult object with the third round matches and the third-placed teams
        return new RoundResult(qualifiedTeams,fourthPlacedTeams, allGroupMatches);
    }

    public RoundResult roundCONMEBOL() {
        List<Team> conmebolTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONMEBOL)
                .collect(Collectors.toList());

        List<List<Team>> groups = createGroups(conmebolTeams, 1, 10);

        List<Match> allGroupMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Team> fifthPlacedTeams = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2015, 10, 8), 10, 3);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }


        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the group winner and runner-up to the respective lists
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
            qualifiedTeams.add(group.get(2));
            qualifiedTeams.add(group.get(3));

            fifthPlacedTeams.add(group.get(4));


        }

        // return a FourthRoundResult object with the third round matches and the third-placed teams
        return new RoundResult(qualifiedTeams,fifthPlacedTeams, allGroupMatches);
    }

    private RoundResult firstRoundOFC() {
        // Filter the teams for those belonging to the OFC region
        List<Team> ofcTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.OFC)
                .filter(team -> Arrays.asList("American Samoa", "Cook Islands", "Samoa", "Tonga").contains(team.getName()))
                .collect(Collectors.toList());

        List<List<Team>> groups = createGroups(ofcTeams, ofcTeams.size() / 2, 2);
        List<Match> firstRoundMatches = new ArrayList<>();
        List<Team> winningTeams = new ArrayList<>();

        for (List<Team> pair : groups) {
            // Generate home and away matches for each pair
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(pair, true);
            firstRoundMatches.addAll(pairedMatches);
        }

        // Assign dates to the first round matches
        assignDatesToRoundMatches(firstRoundMatches, LocalDate.of(2015, 9, 1), 2, 2);

        // Simulate match results
        for (Match match : firstRoundMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = firstRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);
            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }

        // Return a RoundResult object containing the winner and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }

    public RoundResult secondRoundOFC() {
        List<Team> ofcTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.OFC)
                .filter(team -> Arrays.asList("Fiji", "New Caledonia", "New Zealand", "Papua New Guinea", "Solomon Islands", "Tahiti", "Vanuatu" ).contains(team.getName()))
                .collect(Collectors.toList());

        ofcTeams.addAll(getFirstRoundResultOFC().getRoundTeams());

        List<List<Team>> groups = createGroups(ofcTeams, 2, 4);

        List<Match> allGroupMatches = new ArrayList<>();
        List<Team> qualifiedTeams = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> groupMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(groupMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2016, 6, 1), 2, 3);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }


        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the top two teams from each group to the qualified teams
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
            qualifiedTeams.add(group.get(2));

        }

        // return a FourthRoundResult object with the third round matches and the third-placed teams
        return new RoundResult(qualifiedTeams, allGroupMatches);
    }

    public RoundResult thirdRoundOFC() {
        List<List<Team>> groups = createGroups(getSecondRoundResultOFC().getRoundTeams(), 2, 3);

        List<Match> allGroupMatches = new ArrayList<>();
        List<Team> groupWinners = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2016, 11, 7), 23, 2);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the group winner and runner-up to the respective lists
            groupWinners.add(group.get(0));
        }

        // Create a home-and-away match between the two group winners
        Match homeMatch = new Match(groupWinners.get(0), groupWinners.get(1));
        Match awayMatch = new Match(groupWinners.get(1), groupWinners.get(0));

        List<Match> playOffMatches = new ArrayList<>();
        playOffMatches.add(homeMatch);
        playOffMatches.add(awayMatch);

        // Assign dates to the play-off matches
        assignDatesToRoundMatches(playOffMatches, LocalDate.of(2017, 9, 1), 5, 1);

        // Simulate the match results
        homeMatch.simulateMatchResult();
        awayMatch.simulateMatchResult();

        playOffMatches.add(homeMatch);
        playOffMatches.add(awayMatch);

        // Determine the winner of the play-off
        Team playOffWinner = determinePlayOffWinner(playOffMatches);

        // Combine third round matches and play-off matches
        List<Match> allMatches = new ArrayList<>(allGroupMatches);
        allMatches.addAll(playOffMatches);

        // Return a RoundResult object containing the play-off winner and all matches
        return new RoundResult(Collections.singletonList(playOffWinner), allMatches);
    }
    public RoundResult firstRoundUEFA() {
        List<Team> uefaTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.UEFA)
                .collect(Collectors.toList());

        // Divide the teams into nine groups (seven groups of six teams and two groups of five teams)
        List<List<Team>> groupsOfSix = createGroups(uefaTeams.subList(0, 42), 7, 6);
        List<List<Team>> groupsOfFive = createGroups(uefaTeams.subList(42, uefaTeams.size()), 2, 5);

        // Combine both groups
        List<List<Team>> groups = new ArrayList<>(groupsOfSix);
        groups.addAll(groupsOfFive);

        List<Match> allGroupMatches = new ArrayList<>();
        List<Team> groupWinners = new ArrayList<>();
        List<Team> groupRunnersUp = new ArrayList<>();

        // Iterate through each group
        for (List<Team> group : groups) {
            // Arrange home and away matches for the group
            List<Match> secondRoundMatches = arrangeHomeAndAwayMatches(group, true);
            allGroupMatches.addAll(secondRoundMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(allGroupMatches, LocalDate.of(2016, 9, 4), 2, 3);

        // Simulate the matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }



        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> secondRoundMatches = allGroupMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Get the last match date in the group
            LocalDate lastMatchDate = secondRoundMatches.get(secondRoundMatches.size() - 1).getMatchDate();

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getPoints(lastMatchDate), t1.getPoints(lastMatchDate)));

            // Add the group winner and runner-up to the respective lists
            groupWinners.add(group.get(0));
            groupRunnersUp.add(group.get(1));
        }

        // Get the eight best group runners-up
        List<Team> bestGroupRunnersUp = groupRunnersUp.subList(0, 8);

        return new RoundResult(groupWinners, bestGroupRunnersUp, allGroupMatches);
    }

    private RoundResult secondRoundUEFA() {
        // Get the eight best runners-up from the first round
        List<Team> runnersUp = getFirstRoundResultUEFA().getPlayOffTeams();

        List<List<Team>> groups = createGroups(runnersUp, runnersUp.size() / 2, 2);
        List<Team> winningTeams = new ArrayList<>();
        List<Match> firstRoundMatches = new ArrayList<>();

        for (List<Team> pair : groups) {
            // Generate home and away matches for each pair
            List<Match> pairedMatches = arrangeHomeAndAwayMatches(pair, true);
            firstRoundMatches.addAll(pairedMatches);
        }

        // Assign dates to the second round matches
        assignDatesToRoundMatches(firstRoundMatches, LocalDate.of(2017, 11, 9), 2, 2);

        for (List<Team> group : groups) {
            // Get the matches for the current group
            List<Match> allRoundMatches = firstRoundMatches.stream()
                    .filter(match -> group.contains(match.getTeam1()) && group.contains(match.getTeam2()))
                    .collect(Collectors.toList());

            // Determine the winner based on the aggregate score
            int team1Score = allRoundMatches.get(0).getTeam1Score() + allRoundMatches.get(1).getTeam2Score();
            int team2Score = allRoundMatches.get(0).getTeam2Score() + allRoundMatches.get(1).getTeam1Score();
            Team winner = (team1Score > team2Score) ? group.get(0) : group.get(1);

            // Add the winner to the winningTeams list
            winningTeams.add(winner);
        }


        // Return a RoundResult object containing the winners and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }

    public RoundResult playInterConfederationPlayoffs() {
        List<Team> playoffTeams = new ArrayList<>();
        playoffTeams.addAll(getFourthRoundResultAFC().getRoundTeams());
        playoffTeams.addAll(getFifthRoundResultCONCACAF().getPlayOffTeams());
        playoffTeams.addAll(getFirstRoundResultCONMEBOL().getPlayOffTeams());
        playoffTeams.addAll(getThirdRoundResultOFC().getRoundTeams());
        List<Team> worldCupQualifiers = new ArrayList<>();
        List<Match> playOffMatches = new ArrayList<>();

        // Simulate play-offs between the confederations (AFC vs CONCACAF and CONMEBOL vs OFC)
        Team afcVsConcacafWinner = playPlayoffMatch(playoffTeams.get(0), playoffTeams.get(1));
        Team conmebolVsOfcWinner = playPlayoffMatch(playoffTeams.get(2), playoffTeams.get(3));

        // Add the winners to the list of World Cup qualifiers
        worldCupQualifiers.add(afcVsConcacafWinner);
        worldCupQualifiers.add(conmebolVsOfcWinner);

        // Add the played matches to the playOffMatches list
        playOffMatches.add(new Match(playoffTeams.get(0), playoffTeams.get(1)));
        playOffMatches.add(new Match(playoffTeams.get(1), playoffTeams.get(0)));
        playOffMatches.add(new Match(playoffTeams.get(2), playoffTeams.get(3)));
        playOffMatches.add(new Match(playoffTeams.get(3), playoffTeams.get(2)));

        assignDatesToRoundMatches(playOffMatches, LocalDate.of(2017, 11, 10), 2, 1);

        // Simulate the matches
        for (Match match : playOffMatches) {
            match.simulateMatchResult();
        }

        return new RoundResult(worldCupQualifiers, playOffMatches);
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



    private Team determinePlayOffWinner(List<Match> playOffMatches) {
        int homeTeamGoals = playOffMatches.get(0).getTeam1Score() + playOffMatches.get(1).getTeam2Score();
        int awayTeamGoals = playOffMatches.get(0).getTeam1Score() + playOffMatches.get(1).getTeam1Score();

        // Get the home and away teams
        Team homeTeam = playOffMatches.get(0).getTeam1();
        Team awayTeam = playOffMatches.get(0).getTeam2();

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




    private List<List<Team>> createGroups(List<Team> teams, int numberOfGroups, int groupSize) {
        List<List<Team>> groups = new ArrayList<>();

        if (teams.size() < numberOfGroups * groupSize) {
            throw new IllegalArgumentException("Not enough teams to create the specified number of groups.");
        }

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
    public List<Match> arrangeHomeAndAwayMatches(List<Team> teams, boolean homeAndAway) {
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

    private RoundResult firstRoundResultAFC, secondRoundResultAFC, thirdRoundResultAFC, fourthRoundResultAFC;
    private RoundResult firstRoundResultCAF, secondRoundResultCAF, thirdRoundResultCAF;

    private RoundResult firstRoundResultCONCACAF, secondRoundResultCONCACAF, thirdRoundResultCONCACAF,
            fourthRoundResultCONCACAF, fifthRoundResultCONCACAF;
    private RoundResult firstRoundResultCONMEBOL;

    private RoundResult firstRoundResultOFC, secondRoundResultOFC, thirdRoundResultOFC;

    private RoundResult firstRoundResultUEFA, secondRoundResultUEFA;

    private RoundResult playoffResult;


    public RoundResult getPlayoffResult() {
        return playoffResult;
    }

    public RoundResult getFirstRoundResultAFC() {
        return firstRoundResultAFC;
    }
    public RoundResult getSecondRoundResultAFC() {
        return secondRoundResultAFC;
    }
    public RoundResult getThirdRoundResultAFC() {
        return thirdRoundResultAFC;
    }
    public RoundResult getFourthRoundResultAFC() {
        return fourthRoundResultAFC;
    }

    public RoundResult getFirstRoundResultCAF() {
        return firstRoundResultCAF;
    }

    public RoundResult getSecondRoundResultCAF() {
        return secondRoundResultCAF;
    }

    public RoundResult getThirdRoundResultCAF() {
        return thirdRoundResultCAF;
    }

    public RoundResult getFirstRoundResultCONCACAF() {
        return firstRoundResultCONCACAF;
    }

    public RoundResult getSecondRoundResultCONCACAF() {
        return secondRoundResultCONCACAF;
    }

    public RoundResult getThirdRoundResultCONCACAF() {
        return thirdRoundResultCONCACAF;
    }

    public RoundResult getFourthRoundResultCONCACAF() {
        return fourthRoundResultCONCACAF;
    }

    public RoundResult getFifthRoundResultCONCACAF() {
        return fifthRoundResultCONCACAF;
    }

    public RoundResult getFirstRoundResultCONMEBOL() {
        return firstRoundResultCONMEBOL;
    }

    public RoundResult getFirstRoundResultOFC() {
        return firstRoundResultOFC;
    }

    public RoundResult getSecondRoundResultOFC() {
        return secondRoundResultOFC;
    }

    public RoundResult getThirdRoundResultOFC() {
        return thirdRoundResultOFC;
    }

    public RoundResult getFirstRoundResultUEFA() {
        return firstRoundResultUEFA;
    }

    public RoundResult getSecondRoundResultUEFA() {
        return secondRoundResultUEFA;
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
}