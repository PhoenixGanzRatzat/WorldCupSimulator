package Backend.stage;

import Backend.Match;
import Backend.Region;
import Backend.Team;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the Qualifying Stage of a tournament, simulating the FIFA World Cup qualification process.
 * Extends the Stage class and contains a list of qualifier matches.
 * This class provides methods for each round of the qualification stage, each with its own logic,
 * as well as additional helper methods for sorting and arranging matches.
 */
public class QualifyingStage extends Stage {
    List<Match> QualifierMatches;

    /**
     * Constructs a QualifyingStage object with the specified list of teams.
     *
     * @param teams The list of teams participating in the qualifying stage.
     */
    public QualifyingStage(List<Team> teams) {
        super(teams);
        QualifierMatches = new ArrayList<>();
    }

    /**
     * Arranges matches for all rounds and regions in the qualification stage.
     * Calls specific methods to arrange matches for each round and region.
     */
    @Override
    public void arrangeMatches() {
        // Arrange matches for each round and region

        // AFC stands for Asian Football Confederation
        // First Round AFC
        firstRoundResultAFC = firstRoundAFC();
        // Second Round AFC
        secondRoundResultAFC = secondRoundAFC();
        // Third Round AFC
        thirdRoundResultAFC = thirdRoundAFC();
        // Fourth Round AFC
        fourthRoundResultAFC = fourthRoundAFC();

        // CAF stands for Confederation of African Football
        // First Round CAF
        firstRoundResultCAF = firstRoundCAF();
        // Second Round CAF
        secondRoundResultCAF = secondRoundCAF();
        // Third Round CAF
        thirdRoundResultCAF = thirdRoundCAF();

        // CONCACAF stands for Confederation of North, Central America and Caribbean Association Football
        // First Round CONCACAF
        firstRoundResultCONCACAF = firstRoundCONCACAF();
        // Second Round CONCACAF
        secondRoundResultCONCACAF = secondRoundCONCACAF();
        // Third Round CONCACAF
        thirdRoundResultCONCACAF = thirdRoundCONCACAF();
        // Fourth Round CONCACAF
        fourthRoundResultCONCACAF = fourthRoundCONCACAF();
        // Fifth Round CONCACAF
        fifthRoundResultCONCACAF = fifthRoundCONCACAF();

        // CONMEBOL stands for South American Football Confederation
        // First Round CONMEBOL
        firstRoundResultCONMEBOL = roundCONMEBOL();

        // OFC stands for Oceania Football Confederation
        // First Round OFC
        firstRoundResultOFC = firstRoundOFC();
        // Second Round OFC
        secondRoundResultOFC = secondRoundOFC();
        // Third Round OFC
        thirdRoundResultOFC = thirdRoundOFC();

        // UEFA stands for Union of European Football Associations
        // First Round UEFA
        firstRoundResultUEFA = firstRoundUEFA();
        // Second Round UEFA
        secondRoundResultUEFA = secondRoundUEFA();

        // Inter-confederation playoffs
        playoffResult = playInterConfederationPlayoffs();
    }



    /**
     * Retrieves the list of all qualified teams.
     * Creates a host team and adds it to the list.
     * Adds the qualified teams from various rounds and regions to the list.
     *
     * @return The list of all qualified teams.
     */
    public ArrayList<Team> qualifiedTeams(){
        // Create a host team
        Team host = new Team("Russia", "RUS", Region.UEFA, 0);
        ArrayList<Team> qualifiedTeams = new ArrayList<>();

        // Add the host and qualified teams from various rounds and regions
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


    /**
     * Assigns match dates to the provided list of matches based on certain constraints.
     * Matches are scheduled with a specified start date, interval, and maximum number of matches per day.
     * Matches are distributed evenly across allowed months, avoiding specific months if not allowed.
     * Matches are assigned to dates such that each team has a maximum number of matches per day and overall.
     * The match dates are updated in the Match objects, and the maps tracking matches per day and team match dates are updated.
     *
     * @param matches          The list of matches to assign dates to.
     * @param startDate        The start date for scheduling matches.
     * @param interval         The number of days between consecutive matches.
     * @param maxMatchesPerDay The maximum number of matches allowed per day.
     */
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

                int instanceMatchesOnDate = matchesPerDay.getOrDefault(matchDate, 0);
                LocalDate finalMatchDate = matchDate;
                int allMatchesOnDate = (int) getMatches().stream().filter(Match -> Match.getMatchDate().isEqual(finalMatchDate)).count();

                Set<LocalDate> team1MatchDates = teamMatchDates.getOrDefault(match.getTeam1(), new HashSet<>());
                Set<LocalDate> team2MatchDates = teamMatchDates.getOrDefault(match.getTeam2(), new HashSet<>());

                boolean team1HasMatchOnDate = team1MatchDates.stream().anyMatch(date -> date.isEqual(finalMatchDate));
                boolean team2HasMatchOnDate = team2MatchDates.stream().anyMatch(date -> date.isEqual(finalMatchDate));

                // Check if both teams don't have matches on the current date
                if (instanceMatchesOnDate < maxMatchesPerDay && allMatchesOnDate < 6 && !team1HasMatchOnDate && !team2HasMatchOnDate) {
                    // Set the match date and update the maps
                    match.setMatchDate(matchDate);
                    matchesPerDay.put(matchDate, instanceMatchesOnDate + 1);
                    getMatches().add(match);

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

    /**
     * Performs the scheduling and simulation of matches for the provided groups in the round.
     * Arranges home and away matches for each group.
     * Assigns dates to the group matches.
     * Simulates match results for all group matches.
     * Returns a list of all group matches.
     *
     * @param groups          The list of groups containing teams.
     * @param homeAndAway     A boolean value indicating whether to arrange home and away matches.
     * @param startDate       The start date for scheduling matches.
     * @param interval        The interval between matches in days.
     * @param maxMatchesPerDay The maximum number of matches allowed per day.
     * @return The list of all group matches.
     */
    private List<Match> scheduleAndSimulateMatches(List<List<Team>> groups, boolean homeAndAway, LocalDate startDate, int interval, int maxMatchesPerDay) {
        // List to store all group matches
        List<Match> allGroupMatches = new ArrayList<>();

        for (List<Team> group : groups) {
            // Arrange home and away matches for the current group
            List<Match> groupMatches = arrangeHomeAndAwayMatches(group, homeAndAway);
            allGroupMatches.addAll(groupMatches);
        }

        // Assign dates to the group matches
        assignDatesToRoundMatches(allGroupMatches, startDate, interval, maxMatchesPerDay);

        // Simulate match results for all group matches
        for (Match match : allGroupMatches) {
            match.simulateMatchResult();
        }

        return allGroupMatches;
    }

    /**
     * Executes the first round of the AFC qualification stage.
     * Filters the teams belonging to the AFC region and ranked 35-46.
     * Creates groups of teams.
     * Schedules and simulates matches for each group.
     * Determines the winner of each match based on the aggregate score.
     * Returns a RoundResult object containing the winning teams and the matches.
     *
     * @return The RoundResult object representing the first round of the AFC qualification stage.
     */
    private RoundResult firstRoundAFC() {
        // Filter the teams for those belonging to the AFC region and ranked 35-46
        List<Team> firstRoundTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Backend.Region.AFC)
                .filter(team -> team.getRank() >= 35 && team.getRank() <= 46)
                .collect(Collectors.toList());

        // Create groups of teams
        List<List<Team>> groups = createGroups(firstRoundTeams, firstRoundTeams.size() / 2, 2);
        List<Team> winningTeams = new ArrayList<>();

        // Schedule and simulate matches for the groups
        List<Match> firstRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 3, 12), 5, 6);

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

    /**
     * Represents the second round of the AFC qualification stage.
     * This method filters teams, divides them into groups, schedules and simulates matches,
     * determines group winners and runners-up, selects the best runners-up, and identifies
     * the qualified teams for the next round.
     *
     * @return The RoundResult object containing the qualified teams and all group matches.
     */
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
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 6, 11), 3, 4);

        for (List<Team> group : groups) {

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the group winner and runner-up to the respective lists
            groupWinners.add(group.get(0));
            groupRunnersUp.add(group.get(1));
        }

        // Sort the runners-up by their qualifier points in descending order
        groupRunnersUp.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

        // Get the four best group runners-up
        List<Team> bestGroupRunnersUp = groupRunnersUp.subList(0, 4);

        // Combine the winners and the best runners-up
        List<Team> qualifiedTeams = new ArrayList<>(groupWinners);
        qualifiedTeams.addAll(bestGroupRunnersUp);

        // Return a RoundResult object containing the qualified teams and all group matches
        return new RoundResult(qualifiedTeams, allGroupMatches);
    }


    /**
     * Executes the third round of the AFC qualification stage.
     * Divides the teams from the second round into groups of six.
     * Schedules and simulates matches for each group.
     * Sorts the teams within each group based on their qualifier points in descending order.
     * Selects the top two teams from each group as qualified teams.
     * Selects the third-placed team from each group.
     * Returns a RoundResult object containing the qualified teams, third-placed teams, and all group matches.
     *
     * @return The RoundResult object representing the third round of the AFC qualification stage.
     */
    private RoundResult thirdRoundAFC() {
        // Divide the teams from the second round into groups of six
        List<List<Team>> thirdRoundGroups = createGroups(secondRoundResultAFC.getRoundTeams(), 2, 6);
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Team> thirdPlacedTeams = new ArrayList<>();
        List<Match> allGroupMatches = scheduleAndSimulateMatches(thirdRoundGroups, true, LocalDate.of(2016, 9, 1), 11, 2);

        for (List<Team> group : thirdRoundGroups) {
            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the top two teams from each group to the qualified teams
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));

            // Add the third-placed team to the thirdPlacedTeams list
            thirdPlacedTeams.add(group.get(2));
        }

        // Return a RoundResult object with the qualified teams, third-placed teams, and all group matches
        return new RoundResult(qualifiedTeams, thirdPlacedTeams, allGroupMatches);
    }



    /**
     * Represents the fourth round of the AFC qualification stage.
     * It involves a play-off between two third-placed teams from the third round groups.
     * The winner of the play-off advances to the next round.
     *
     * @return The RoundResult object containing the play-off winner and the fourth round matches.
     */
    public RoundResult fourthRoundAFC() {
        // Two third-placed teams from the third round groups
        Team team1 = getThirdRoundResultAFC().getPlayoffTeams().get(0);
        Team team2 = getThirdRoundResultAFC().getPlayoffTeams().get(1);

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

        // Return a RoundResult object containing the play-off winner and the fourth round matches
        return new RoundResult(Collections.singletonList(playOffWinner), fourthRoundMatches);
    }


    /**
     * Executes the first round of the CAF (Confederation of African Football) qualification stage.
     * Teams with a rank between 28 and 53 (inclusive) are filtered from the available teams.
     * The filtered teams are divided into groups, matches are scheduled and simulated,
     * and the winners of each group are determined based on the aggregate score of their matches.
     *
     * @return a RoundResult object containing the winning teams and the matches played in the first round
     */
    public RoundResult firstRoundCAF() {
        // Filter teams with a rank between 28 and 53 (inclusive)
        List<Team> cafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Backend.Region.CAF)
                .filter(team -> team.getRank() >= 28 && team.getRank() <= 53)
                .collect(Collectors.toList());

        // Divide the teams into groups
        List<List<Team>> groups = createGroups(cafTeams, cafTeams.size() / 2, 2);
        List<Team> winningTeams = new ArrayList<>();

        // Schedule and simulate matches for the groups
        List<Match> firstRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 10, 7), 1, 6);

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

        // Return a RoundResult object containing the winning teams and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }


    /**
     * Executes the second round of the CAF qualification stage.
     * Filters the teams belonging to the CAF region and limits the selection to the top 27 teams.
     * Adds the winners from the first round to the CAF teams.
     * Creates groups by dividing the CAF teams into pairs, aiming for an equal number of teams in each group.
     * Schedules and simulates matches for each group.
     * Determines the winner of each match based on the aggregate score.
     * Returns a RoundResult object containing the winning teams and the matches.
     *
     * @return The RoundResult object representing the second round of the CAF qualification stage.
     */
    public RoundResult secondRoundCAF() {
        // Filter the teams for those belonging to the CAF region and limit to the top 27 teams
        List<Team> cafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Backend.Region.CAF)
                .sorted(Comparator.comparing(Team::getRank))
                .limit(27)
                .collect(Collectors.toList());

        // Add the 6 first round winners to the CAF teams
        cafTeams.addAll(firstRoundResultCAF.getRoundTeams());

        // Create groups by dividing the CAF teams into pairs, aiming for an equal number of teams in each group
        List<List<Team>> groups = createGroups(cafTeams, cafTeams.size() / 2, 2);

        // Schedule and simulate matches for the groups
        List<Match> secondRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 11, 9), 1, 6);

        List<Team> winningTeams = new ArrayList<>();

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

        // Return a RoundResult object containing the winning teams and the matches in the second round
        return new RoundResult(winningTeams, secondRoundMatches);
    }



    /**
     * Executes the third round of the CAF qualification stage.
     * Divides the teams into eight groups of five teams.
     * Schedules and simulates matches for each group.
     * Determines the group winners based on the qualifier points.
     * Returns a RoundResult object containing the qualified teams and the matches.
     *
     * @return The RoundResult object representing the third round of the CAF qualification stage.
     */
    public RoundResult thirdRoundCAF() {
        // Divide the teams into eight groups of five teams
        List<List<Team>> groups = createGroups(secondRoundResultCAF.getRoundTeams(), 5, 4);
        List<Team> groupWinners = new ArrayList<>();
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2016, 10, 7), 9, 3);

        for (List<Team> group : groups) {

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the group winner to the groupWinners list
            groupWinners.add(group.get(0));
        }

        // Combine the group winners to form the list of qualified teams
        List<Team> qualifiedTeams = new ArrayList<>(groupWinners);

        // Return a RoundResult object containing the qualified teams and all group matches
        return new RoundResult(qualifiedTeams, allGroupMatches);
    }


    /**
     * Executes the first round of the CONCACAF qualification stage.
     * Filters the teams belonging to the CONCACAF region and ranked 22-35.
     * Pairs the teams into groups of two and schedules and simulates matches for each group.
     * Determines the winner of each match based on the aggregate score.
     * Returns a RoundResult object containing the winning teams and the matches.
     *
     * @return The RoundResult object representing the first round of the CONCACAF qualification stage.
     */
    private RoundResult firstRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 22-35
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 22 && team.getRank() <= 35)
                .collect(Collectors.toList());

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, concacafTeams.size() / 2, 2);

        // Schedule and simulate matches for the groups
        List<Match> firstRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 3, 1), 4, 2);
        List<Team> winningTeams = new ArrayList<>();

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

    /**
     * Executes the second round of the CONCACAF qualification stage.
     * Filters the teams belonging to the CONCACAF region and ranked 9-21.
     * Pairs the teams into groups of two and schedules and simulates matches for each group.
     * Determines the winner of each match based on the aggregate score.
     * Returns a RoundResult object containing the winning teams and the matches.
     *
     * @return The RoundResult object representing the second round of the CONCACAF qualification stage.
     */
    private RoundResult secondRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 9-21
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 9 && team.getRank() <= 21)
                .collect(Collectors.toList());

        // Add the teams from the first round
        concacafTeams.addAll(getFirstRoundResultCONCACAF().getRoundTeams());

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, concacafTeams.size() / 2, 2);

        // Schedule and simulate matches for the groups
        List<Match> secondRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 6, 1), 3, 2);
        List<Team> winningTeams = new ArrayList<>();

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

    /**
     * Executes the third round of the CONCACAF (Confederation of North, Central America and Caribbean Association Football) qualification stage.
     * Teams belonging to the CONCACAF region and ranked 7-8 are filtered from the available teams.
     * The filtered teams are paired into groups, matches are scheduled and simulated,
     * and the winners of each group are determined based on the aggregate score of their matches.
     *
     * @return a RoundResult object containing the winning teams and the matches played in the third round
     */
    private RoundResult thirdRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 7-8
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 7 && team.getRank() <= 8)
                .collect(Collectors.toList());

        // Add the teams from the second round of the CONCACAF qualification
        concacafTeams.addAll(getSecondRoundResultCONCACAF().getRoundTeams());

        // Pair the teams into groups of two
        List<List<Team>> groups = createGroups(concacafTeams, concacafTeams.size() / 2, 2);
        List<Match> thirdRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 9, 1), 5, 2);
        List<Team> winningTeams = new ArrayList<>();

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

        // Return a RoundResult object containing the winning teams and the matches
        return new RoundResult(winningTeams, thirdRoundMatches);
    }


    /**
     * Executes the fourth round of the CONCACAF (Confederation of North, Central America and Caribbean Association Football) qualification stage.
     * Teams belonging to the CONCACAF region and ranked 1-6, as well as the qualified teams from the third round, are filtered from the available teams.
     * The filtered teams are divided into groups, matches are scheduled and simulated,
     * and the winners of each group are determined based on their qualifier points.
     *
     * @return a RoundResult object containing the qualified teams and the matches played in the fourth round
     */
    private RoundResult fourthRoundCONCACAF() {
        // Filter the teams for those belonging to the CONCACAF region and ranked 1-6
        List<Team> concacafTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONCACAF)
                .filter(team -> team.getRank() >= 1 && team.getRank() <= 6)
                .collect(Collectors.toList());

        // Add the teams that qualified from the third round
        concacafTeams.addAll(getThirdRoundResultCONCACAF().getRoundTeams());

        // Divide the teams into groups
        List<List<Team>> groups = createGroups(concacafTeams, 3, 4);
        List<Team> qualifiedTeams = new ArrayList<>();

        // Schedule and simulate matches for the groups
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 11, 1), 3, 2);

        for (List<Team> group : groups) {

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the top teams to the qualifiedTeams list
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
        }

        // Return a RoundResult object containing the qualified teams and the matches
        return new RoundResult(qualifiedTeams, allGroupMatches);
    }


    /**
     * Executes the fifth round of the CONCACAF (Confederation of North, Central America and Caribbean Association Football) qualification stage.
     * The teams from the fourth round are divided into groups, matches are scheduled and simulated,
     * and the qualified teams and fourth-placed teams are determined based on their performance in the matches.
     *
     * @return a RoundResult object containing the qualified teams, fourth-placed teams, and the matches played in the fifth round
     */
    private RoundResult fifthRoundCONCACAF() {
        // Divide the teams from the fourth round into groups
        List<List<Team>> groups = createGroups(getFourthRoundResultCONCACAF().getRoundTeams(), 1, 6);
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Team> fourthPlacedTeams = new ArrayList<>();

        // Schedule and simulate matches for the groups
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2016, 11, 2), 10, 2);

        for (List<Team> group : groups) {

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the top teams to the qualified lists
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
            qualifiedTeams.add(group.get(2));

            // Add the fourth placed teams to the respective lists
            fourthPlacedTeams.add(group.get(3));
        }

        // Return a RoundResult object containing the qualified teams, fourth-placed teams, and the matches
        return new RoundResult(qualifiedTeams, fourthPlacedTeams, allGroupMatches);
    }


    /**
     * Executes the round of the CONMEBOL (South American Football Confederation) qualification stage.
     * All teams belonging to the CONMEBOL region are selected.
     * The selected teams are divided into a single group of 10 teams.
     * Matches are scheduled and simulated for the group, and the qualified teams and fifth-placed teams are determined.
     *
     * @return a RoundResult object containing the qualified teams, fifth-placed teams, and the matches played in the round
     */
    public RoundResult roundCONMEBOL() {
        // Filter teams belonging to the CONMEBOL region
        List<Team> conmebolTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.CONMEBOL)
                .collect(Collectors.toList());

        // Divide the teams into a single group
        List<List<Team>> groups = createGroups(conmebolTeams, 1, 10);

        // Schedule and simulate matches for the group
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 10, 8), 10, 3);
        List<Team> qualifiedTeams = new ArrayList<>();
        List<Team> fifthPlacedTeams = new ArrayList<>();

        for (List<Team> group : groups) {

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the group winner, runner-up, and third-placed teams to the qualifiedTeams list
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
            qualifiedTeams.add(group.get(2));
            qualifiedTeams.add(group.get(3));

            // Add the fifth-placed team to the fifthPlacedTeams list
            fifthPlacedTeams.add(group.get(4));
        }

        // Return a RoundResult object containing the qualified teams, fifth-placed teams, and the matches
        return new RoundResult(qualifiedTeams, fifthPlacedTeams, allGroupMatches);
    }


    /**
     * Executes the first round of the OFC (Oceania Football Confederation) qualification stage.
     * Teams belonging to the OFC region are filtered based on their names.
     * The filtered teams are divided into groups, matches are scheduled and simulated,
     * and the winners of each group are determined based on the aggregate score of their matches.
     *
     * @return a RoundResult object containing the winning teams and the matches played in the first round
     */
    private RoundResult firstRoundOFC() {
        // Filter the teams for those belonging to the OFC region
        List<Team> ofcTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.OFC)
                .filter(team -> Arrays.asList("American Samoa", "Cook Islands", "Samoa", "Tonga").contains(team.getName()))
                .collect(Collectors.toList());

        // Divide the teams into groups
        List<List<Team>> groups = createGroups(ofcTeams, ofcTeams.size() / 2, 2);
        List<Match> firstRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2015, 9, 1), 2, 2);
        List<Team> winningTeams = new ArrayList<>();

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

        // Return a RoundResult object containing the winning teams and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }


    /**
     * Executes the second round of the OFC (Oceania Football Confederation) qualification stage.
     * Teams belonging to the OFC region are filtered based on their names.
     * The filtered teams are combined with the teams from the first round of OFC qualification.
     * The combined teams are divided into groups, matches are scheduled and simulated,
     * and the top teams from each group are selected as qualified teams.
     *
     * @return a RoundResult object containing the qualified teams and the matches played in the second round
     */
    public RoundResult secondRoundOFC() {
        // Filter teams belonging to the OFC region based on their names
        List<Team> ofcTeams = getTeams().stream()
                .filter(team -> team.getRegion() == Region.OFC)
                .filter(team -> Arrays.asList("Fiji", "New Caledonia", "New Zealand", "Papua New Guinea", "Solomon Islands", "Tahiti", "Vanuatu").contains(team.getName()))
                .collect(Collectors.toList());

        // Combine the OFC teams with the teams from the first round of OFC qualification
        ofcTeams.addAll(getFirstRoundResultOFC().getRoundTeams());

        // Divide the teams into groups
        List<List<Team>> groups = createGroups(ofcTeams, 2, 4);
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2016, 6, 1), 2, 3);
        List<Team> qualifiedTeams = new ArrayList<>();

        for (List<Team> group : groups) {

            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the top teams from each group to the qualified teams
            qualifiedTeams.add(group.get(0));
            qualifiedTeams.add(group.get(1));
            qualifiedTeams.add(group.get(2));
        }

        // Return a RoundResult object containing the qualified teams and the matches
        return new RoundResult(qualifiedTeams, allGroupMatches);
    }

    /**
     * Executes the third round of the OFC (Oceania Football Confederation) qualification stage.
     * The second round result is used to create groups, matches are scheduled and simulated,
     * and the group winners compete in a home-and-away playoff.
     * The winner of the playoff is determined and returned along with all the matches played in the round.
     *
     * @return a RoundResult object containing the playoff winner and all matches played in the third round
     */
    public RoundResult thirdRoundOFC() {
        // Create groups from the teams in the second round result
        List<List<Team>> groups = createGroups(getSecondRoundResultOFC().getRoundTeams(), 2, 3);

        // Schedule and simulate matches for the groups
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2016, 11, 7), 23, 2);
        List<Team> groupWinners = new ArrayList<>();

        for (List<Team> group : groups) {
            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the group winner to the groupWinners list
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

        // Add the play-off matches to the list of all matches
        playOffMatches.add(homeMatch);
        playOffMatches.add(awayMatch);

        // Determine the winner of the play-off
        Team playOffWinner = determinePlayOffWinner(playOffMatches);

        // Combine all group matches and play-off matches
        List<Match> allMatches = new ArrayList<>(allGroupMatches);
        allMatches.addAll(playOffMatches);

        // Return a RoundResult object containing the play-off winner and all matches
        return new RoundResult(Collections.singletonList(playOffWinner), allMatches);
    }

    /**
     * Executes the first round of the UEFA (Union of European Football Associations) qualification stage.
     * All teams belonging to the UEFA region are included in the qualification process.
     * The teams are divided into nine groups, consisting of seven groups with six teams each
     * and two groups with five teams each. Matches are scheduled and simulated for each group,
     * and the winners and best runners-up from each group are determined based on their qualifier points.
     *
     * @return a RoundResult object containing the group winners, best runners-up, and the matches played in the first round
     */
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

        // Schedule and simulate matches for the groups
        List<Match> allGroupMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2016, 9, 4), 2, 3);
        List<Team> groupWinners = new ArrayList<>();
        List<Team> groupRunnersUp = new ArrayList<>();

        for (List<Team> group : groups) {
            // Sort the teams in the group by their qualifier points in descending order
            group.sort((t1, t2) -> Integer.compare(t2.getMostRecentScore(), t1.getMostRecentScore()));

            // Add the group winner and runner-up to the respective lists
            groupWinners.add(group.get(0));
            groupRunnersUp.add(group.get(1));
        }

        // Get the eight best group runners-up
        List<Team> bestGroupRunnersUp = groupRunnersUp.subList(0, 8);

        // Return a RoundResult object containing the group winners, best runners-up, and the matches
        return new RoundResult(groupWinners, bestGroupRunnersUp, allGroupMatches);
    }


    /**
     * Executes the second round of the UEFA (Union of European Football Associations) qualification stage.
     * The eight best runners-up from the first round are retrieved.
     * The runners-up are divided into groups, matches are scheduled and simulated,
     * and the winners of each group are determined based on the aggregate score of their matches.
     *
     * @return a RoundResult object containing the winning teams and the matches played in the second round
     */
    private RoundResult secondRoundUEFA() {
        // Get the eight best runners-up from the first round
        List<Team> runnersUp = getFirstRoundResultUEFA().getPlayoffTeams();

        // Divide the runners-up into groups
        List<List<Team>> groups = createGroups(runnersUp, runnersUp.size() / 2, 2);
        List<Team> winningTeams = new ArrayList<>();

        // Schedule and simulate matches for the groups
        List<Match> firstRoundMatches = scheduleAndSimulateMatches(groups, true, LocalDate.of(2017, 11, 9), 2, 2);

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

        // Return a RoundResult object containing the winning teams and the matches
        return new RoundResult(winningTeams, firstRoundMatches);
    }


    /**
     * Plays the inter-confederation playoffs to determine the additional World Cup qualifiers.
     * Teams from different confederations participate in the playoffs.
     * The playoff teams are retrieved from various rounds of the qualification stage.
     * Matches are simulated between the teams, and the winners are determined.
     *
     * @return a RoundResult object containing the World Cup qualifiers and the playoff matches played
     */
    public RoundResult playInterConfederationPlayoffs() {
        // Retrieve the playoff teams from different confederations
        List<Team> playoffTeams = new ArrayList<>();
        playoffTeams.addAll(getFourthRoundResultAFC().getRoundTeams());
        playoffTeams.addAll(getFifthRoundResultCONCACAF().getPlayoffTeams());
        playoffTeams.addAll(getFirstRoundResultCONMEBOL().getPlayoffTeams());
        playoffTeams.addAll(getThirdRoundResultOFC().getRoundTeams());

        List<Team> worldCupQualifiers = new ArrayList<>();
        List<Match> playOffMatches = new ArrayList<>();

        // Simulate play-offs between the confederations (AFC vs CONCACAF and CONMEBOL vs OFC)
        playOffMatches.addAll(playPlayoffMatch(playoffTeams.get(0), playoffTeams.get(1)));
        playOffMatches.addAll(playPlayoffMatch(playoffTeams.get(2), playoffTeams.get(3)));

        // Assign dates to the playoff matches starting from November 10, 2017, with an interval of 2 days and a maximum of 1 match per day
        assignDatesToRoundMatches(playOffMatches, LocalDate.of(2017, 11, 10), 2, 1);

        // Simulate the matches
        for (Match match : playOffMatches) {
            match.simulateMatchResult();
        }

        // Determine the winners of the playoffs
        Team afcVsConcacafWinner = determinePlayOffWinner(playOffMatches.subList(0, 2));
        Team conmebolVsOfcWinner = determinePlayOffWinner(playOffMatches.subList(2, 4));

        // Add the winners to the list of World Cup qualifiers
        worldCupQualifiers.add(afcVsConcacafWinner);
        worldCupQualifiers.add(conmebolVsOfcWinner);

        // Return a RoundResult object containing the World Cup qualifiers and the playoff matches
        return new RoundResult(worldCupQualifiers, playOffMatches);
    }



    /**
     * Simulates a playoff match between two teams.
     * Creates a home match and an away match between the two teams.
     * The winner of the playoff match is determined using the determinePlayOffWinner method.
     *
     * @param team1 the first team in the playoff match
     * @param team2 the second team in the playoff match
     * @return a list of two matches representing the home and away matches of the playoff
     */
    public List<Match> playPlayoffMatch(Team team1, Team team2) {
        // Create a home match and an away match between the two teams
        Match homeMatch = new Match(team1, team2);
        Match awayMatch = new Match(team2, team1);

        // Determine the winner using the determinePlayOffWinner method
        List<Match> playOffMatches = Arrays.asList(homeMatch, awayMatch);

        return playOffMatches;
    }

    /**
     * Determines the winner of a playoff round based on the results of the playoff matches.
     * The home team and away team goals are compared to determine the winner.
     * If the goals are tied, the away goals rule is used.
     * If the away goals are also tied, a random winner is chosen through a penalty shootout.
     *
     * @param playOffMatches the list of playoff matches
     * @return the winning team of the playoff round
     */
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
        return Math.random() > 0.5 ? homeTeam : awayTeam;
    }


    /**
     * Creates groups of teams from a given list, based on the specified number of groups and group size.
     *
     * @param teams          The list of teams to be divided into groups.
     * @param numberOfGroups The desired number of groups.
     * @param groupSize      The size of each group.
     * @return A list of lists representing the groups of teams.
     * @throws IllegalArgumentException if there are not enough teams to create the specified number of groups.
     */
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

    //Represents the results of the AFC qualification stage.
    private RoundResult firstRoundResultAFC, secondRoundResultAFC, thirdRoundResultAFC, fourthRoundResultAFC;

    //Represents the results of the CAF qualification stage.
    private RoundResult firstRoundResultCAF, secondRoundResultCAF, thirdRoundResultCAF;

    //Represents the results of the CONCACAF qualification stage.
    private RoundResult firstRoundResultCONCACAF, secondRoundResultCONCACAF, thirdRoundResultCONCACAF,
            fourthRoundResultCONCACAF, fifthRoundResultCONCACAF;
    //Represents the results of the CONMEBOL qualification stage.
    private RoundResult firstRoundResultCONMEBOL;

    //Represents the results of the OFC qualification stage.
    private RoundResult firstRoundResultOFC, secondRoundResultOFC, thirdRoundResultOFC;

    //Represents the results of the UEFA qualification stage.
    private RoundResult firstRoundResultUEFA, secondRoundResultUEFA;

    //Represents the results of the Play-off qualification stage.
    private RoundResult playoffResult;


    /**
     * Retrieves the playoff round result.
     *
     * @return The RoundResult object representing the playoff round.
     */
    public RoundResult getPlayoffResult() {
        return playoffResult;
    }

    /**
     * Retrieves the result of the first round in AFC qualification.
     *
     * @return The RoundResult object representing the first round in AFC qualification.
     */
    public RoundResult getFirstRoundResultAFC() {
        return firstRoundResultAFC;
    }

    /**
     * Retrieves the result of the second round in AFC qualification.
     *
     * @return The RoundResult object representing the second round in AFC qualification.
     */
    public RoundResult getSecondRoundResultAFC() {
        return secondRoundResultAFC;
    }

    /**
     * Retrieves the result of the third round in AFC qualification.
     *
     * @return The RoundResult object representing the third round in AFC qualification.
     */
    public RoundResult getThirdRoundResultAFC() {
        return thirdRoundResultAFC;
    }

    /**
     * Retrieves the result of the fourth round in AFC qualification.
     *
     * @return The RoundResult object representing the fourth round in AFC qualification.
     */
    public RoundResult getFourthRoundResultAFC() {
        return fourthRoundResultAFC;
    }

    /**
     * Retrieves the result of the first round in CAF qualification.
     *
     * @return The RoundResult object representing the first round in CAF qualification.
     */
    public RoundResult getFirstRoundResultCAF() {
        return firstRoundResultCAF;
    }

    /**
     * Retrieves the result of the second round in CAF qualification.
     *
     * @return The RoundResult object representing the second round in CAF qualification.
     */
    public RoundResult getSecondRoundResultCAF() {
        return secondRoundResultCAF;
    }

    /**
     * Retrieves the result of the third round in CAF qualification.
     *
     * @return The RoundResult object representing the third round in CAF qualification.
     */
    public RoundResult getThirdRoundResultCAF() {
        return thirdRoundResultCAF;
    }

    /**
     * Retrieves the result of the first round in CONCACAF qualification.
     *
     * @return The RoundResult object representing the first round in CONCACAF qualification.
     */
    public RoundResult getFirstRoundResultCONCACAF() {
        return firstRoundResultCONCACAF;
    }

    /**
     * Retrieves the result of the second round in CONCACAF qualification.
     *
     * @return The RoundResult object representing the second round in CONCACAF qualification.
     */
    public RoundResult getSecondRoundResultCONCACAF() {
        return secondRoundResultCONCACAF;
    }

    /**
     * Retrieves the result of the third round in CONCACAF qualification.
     *
     * @return The RoundResult object representing the third round in CONCACAF qualification.
     */
    public RoundResult getThirdRoundResultCONCACAF() {
        return thirdRoundResultCONCACAF;
    }

    /**
     * Retrieves the result of the fourth round in CONCACAF qualification.
     *
     * @return The RoundResult object representing the fourth round in CONCACAF qualification.
     */
    public RoundResult getFourthRoundResultCONCACAF() {
        return fourthRoundResultCONCACAF;
    }

    /**
     * Retrieves the result of the fifth round in CONCACAF qualification.
     *
     * @return The RoundResult object representing the fifth round in CONCACAF qualification.
     */
    public RoundResult getFifthRoundResultCONCACAF() {
        return fifthRoundResultCONCACAF;
    }

    /**
     * Retrieves the result of the first round in CONMEBOL qualification.
     *
     * @return The RoundResult object representing the first round in CONMEBOL qualification.
     */
    public RoundResult getFirstRoundResultCONMEBOL() {
        return firstRoundResultCONMEBOL;
    }

    /**
     * Retrieves the result of the First round in OFC qualification.
     *
     * @return The RoundResult object representing the first round in OFC qualification.
     */
    public RoundResult getFirstRoundResultOFC() {
        return firstRoundResultOFC;
    }

    /**
     * Retrieves the result of the second round in OFC qualification.
     *
     * @return The RoundResult object representing the second round in OFC qualification.
     */
    public RoundResult getSecondRoundResultOFC() {
        return secondRoundResultOFC;
    }

    /**
     * Retrieves the result of the third round in OFC qualification.
     *
     * @return The RoundResult object representing the third round in OFC qualification.
     */
    public RoundResult getThirdRoundResultOFC() {
        return thirdRoundResultOFC;
    }

    /**
     * Retrieves the result of the first round in UEFA qualification.
     *
     * @return The RoundResult object representing the first round in UEFA qualification.
     */
    public RoundResult getFirstRoundResultUEFA() {
        return firstRoundResultUEFA;
    }

    /**
     * Retrieves the result of the second round in UEFA qualification.
     *
     * @return The RoundResult object representing the second round in UEFA qualification.
     */
    public RoundResult getSecondRoundResultUEFA() {
        return secondRoundResultUEFA;
    }

    /**
     * Represents the result of a round in the qualification stage.
     * Provides access to the teams and matches of the round.
     */
    public class RoundResult {
        private List<Team> roundTeams;
        private List<Team> playoffTeams;
        private List<Match> roundMatches;

        /**
         * Constructs a RoundResult object with the winners, playoff teams, and matches of the round.
         *
         * @param winners      The list of teams that won the round.
         * @param playoffTeams The list of teams that advanced to the playoffs.
         * @param matches      The list of matches played in the round.
         */
        public RoundResult(List<Team> winners, List<Team> playoffTeams, List<Match> matches) {
            this.roundTeams = winners;
            this.playoffTeams = playoffTeams;
            this.roundMatches = matches;
        }

        /**
         * Constructs a RoundResult object with the winners and matches of the round.
         *
         * @param winners The list of teams that won the round.
         * @param matches The list of matches played in the round.
         */
        public RoundResult(List<Team> winners, List<Match> matches) {
            this.roundTeams = winners;
            this.roundMatches = matches;
        }

        /**
         * Retrieves the list of playoff teams from the round.
         *
         * @return The list of playoff teams.
         */
        public List<Team> getPlayoffTeams() {

            return playoffTeams;
        }

        /**
         * Sets the list of playoff teams for the round.
         *
         * @param playoffTeams The list of playoff teams.
         */
        public void setPlayoffTeams(List<Team> playoffTeams) {
            this.playoffTeams = playoffTeams;
        }

        /**
         * Retrieves the list of teams that won the round.
         *
         * @return The list of winning teams.
         */
        public List<Team> getRoundTeams() {
            return roundTeams;
        }

        /**
         * Retrieves the list of matches played in the round.
         *
         * @return The list of matches.
         */
        public List<Match> getRoundMatches() {
            return roundMatches;
        }
    }
}