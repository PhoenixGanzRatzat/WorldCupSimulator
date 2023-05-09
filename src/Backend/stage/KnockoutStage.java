package Backend.stage;

import Backend.Match;
import Backend.Team;
import Backend.exception.TeamListNotSizedProperlyException;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>A class representing the knockout stage of the World Cup.
 * The knockout stage has five rounds of matches:</p>
 * <ul>
 * <li>The Round of 16</li>
 * <li>The Quarter-finals</li>
 * <li>The Semi-finals</li>
 * <li>The Final match</li>
 * <li>The Third-place playoff</li>
 * <ul>
 * <p>Each of these rounds are represented as a list of matches.</p>
 *
 * @author Tre Logan
 */
public class KnockoutStage extends Stage {

    private static final int WORLD_CUP_YEAR = 2018;
    private static final int NUM_TEAMS_IN_ROUND_OF_SIXTEEN = 16;
    private static final int NUM_TEAMS_IN_QUARTERFINALS = 8;
    private static final int NUM_TEAMS_IN_SEMIFINALS = 4;
    private static final int NUM_TEAMS_IN_FINALS = 2;
    private List<Match> roundOfSixteenMatches;
    private List<Match> quarterfinalsMatches;
    private List<Match> semifinalsMatches;
    private Match finalsMatch;
    private Match thirdPlaceMatch;
    private Team firstPlaceTeam;
    private Team secondPlaceTeam;
    private Team thirdPlaceTeam;

    /**
     * Creates a knockout stage containing the specified initial list of teams not exceeding a size of 16 and with
     * all rounds initialized to an empty list of matches.
     * @param teams The teams participating in this knockout stage.
     */
    public KnockoutStage(List<Team> teams) {
        super(teams);
        if (isTeamListNotSizedProperly(teams))
            throw new TeamListNotSizedProperlyException("Can't create matches from an oddly counted, or empty, list of teams.");
        roundOfSixteenMatches = new ArrayList<>();
        quarterfinalsMatches = new ArrayList<>();
        semifinalsMatches = new ArrayList<>();
    }

    @Override
    public void arrangeMatches() {
        roundOfSixteenMatches = createMatchesFromTeams(getTeams());
    }

    /**
     * Checks if the list of teams provided to this knockout stage is an odd count, or if it's empty.
     * @return True if the list of teams is empty, or if the list contains an odd number of teams.
     */
    private boolean isTeamListNotSizedProperly(List<Team> teams) {
        return teams.isEmpty() || isNotEvenNumberOfTeams(teams);
    }

    /**
     * Checks if the list of teams provided to this knockout stage is counted oddly.
     * @return True if the list contains an odd count of teams.
     */
    private boolean isNotEvenNumberOfTeams(List<Team> teams) {
        return teams.size() % 2 != 0;
    }

    /**
     * Creates a list of matches from the list of teams provided.
     * @param teams The teams used for the matches.
     * @return A list of non-simulated matches.
     */
    private List<Match> createMatchesFromTeams(List<Team> teams) {
        List<Match> matches = new ArrayList<>();
        List<LocalDate> matchDates = getMatchDatesFromNumberOfTeams(teams.size());
        createMatchesAndPopulateMatchList(matches, teams, matchDates);
        return matches;
    }

    /**
     * Returns a list of dates for the round containing the specified number of teams provided as an argument.
     * @param numberOfTeams The number of teams provided a parameter for determining which round to get dates for.
     * @return A list of dates for the round determined by the number of teams.
     */
    private List<LocalDate> getMatchDatesFromNumberOfTeams(int numberOfTeams) {
        switch (numberOfTeams) {
            case NUM_TEAMS_IN_ROUND_OF_SIXTEEN:
                return getMatchDatesForRoundOfSixteen();
            case NUM_TEAMS_IN_QUARTERFINALS:
                return getMatchDatesForQuarterfinals();
            case NUM_TEAMS_IN_SEMIFINALS:
                return getMatchDatesForSemifinals();
            case NUM_TEAMS_IN_FINALS:
                return Collections.singletonList(LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 15));
        }
        return Collections.emptyList();
    }

    /**
     * Creates matches using the provided list of teams and dates, populating the given list of matches in the process.
     * @param matches The match list to populate.
     * @param teams The teams used to created matches.
     * @param matchDates The list of dates to get applied to matches depending on team list size and index.
     */
    private void createMatchesAndPopulateMatchList(List<Match> matches, List<Team> teams, List<LocalDate> matchDates) {
        for (int i = 0; i < teams.size(); i += 2) {
            LocalDate matchDate = teams.size() <= NUM_TEAMS_IN_SEMIFINALS ? matchDates.get(i / 2) : matchDates.get(i / 4);
            Match match = new Match(teams.get(i), teams.get(i + 1), matchDate);
            matches.add(match);
        }
    }

    private List<LocalDate> getMatchDatesForRoundOfSixteen() {
        return Arrays.asList(
                LocalDate.of(WORLD_CUP_YEAR, Month.JUNE.getValue(), 30),
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 1),
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 2),
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 3)
        );
    }

    private List<LocalDate> getMatchDatesForQuarterfinals() {
        return Arrays.asList(
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 6),
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 7),
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 8)
        );
    }

    private List<LocalDate> getMatchDatesForSemifinals() {
        return Arrays.asList(
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 10),
                LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 11)
        );
    }

    @Override
    public void calculateMatchResults() {
        simulateAllStageRounds();
    }

    /**
     * Simulates all the rounds for the knockout stage, assigning first, second, and third place winners.
     */
    private void simulateAllStageRounds() {
        simulateRoundOfSixteen();
        List<Team> lastMatchWinners = getWinningTeamsOfMatchResults(roundOfSixteenMatches);

        simulateQuarterfinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(quarterfinalsMatches);

        simulateSemifinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(semifinalsMatches);
        List<Team> lastMatchLosers = getLosingTeamsOfMatches(semifinalsMatches);
        thirdPlaceMatch = simulateThirdPlacePlayoffMatch(lastMatchLosers.get(0), lastMatchLosers.get(1));
        thirdPlaceTeam = thirdPlaceMatch.getWinner();

        finalsMatch = simulateFinalsMatch(lastMatchWinners.get(0), lastMatchWinners.get(1));
        firstPlaceTeam = finalsMatch.getWinner();
        secondPlaceTeam = finalsMatch.getLoser();
    }

    /**
     * Simulates all matches in the Round of 16.
     */
    private void simulateRoundOfSixteen() {
        roundOfSixteenMatches.forEach(Match::simulateMatchResult);
    }

    /**
     * Simulates the quarter-final matches using the provided list of teams.
     * @param teams The teams to be contained in the list of quarter-final matches.
     */
    private void simulateQuarterfinals(List<Team> teams) {
        quarterfinalsMatches = createMatchesFromTeams(teams);
        quarterfinalsMatches.forEach(Match::simulateMatchResult);
    }

    /**
     * Simulates the semi-final matches using the provided list of teams.
     * @param teams The teams to be contained in the list of semi-final matches.
     */
    private void simulateSemifinals(List<Team> teams) {
        semifinalsMatches = createMatchesFromTeams(teams);
        semifinalsMatches.forEach(Match::simulateMatchResult);
    }

    /**
     * Simulates the final match using the given two teams as competitors.
     * @return A simulated match.
     */
    private Match simulateFinalsMatch(Team teamOne, Team teamTwo) {
        LocalDate matchDate = LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 15);
        Match match = new Match(teamOne, teamTwo, matchDate, true);
        match.simulateMatchResult();
        return match;
    }

    /**
     * Simulates the third place play-off match between two teams.
     * @return A simulated match.
     */
    private Match simulateThirdPlacePlayoffMatch(Team teamOne, Team teamTwo) {
        LocalDate matchDate = LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 14);
        Match match = new Match(teamOne, teamTwo, matchDate, true);
        match.simulateMatchResult();
        return match;
    }

    private List<Team> getWinningTeamsOfMatchResults(List<Match> match) {
        return match.stream().map(Match::getWinner).collect(Collectors.toList());
    }

    private List<Team> getLosingTeamsOfMatches(List<Match> matches) {
        return matches.stream().map(Match::getLoser).collect(Collectors.toList());
    }

    public List<Match> getMatchesForRoundOfSixteen() {
        return roundOfSixteenMatches;
    }

    public List<Match> getMatchesForQuarterfinals() {
        return quarterfinalsMatches;
    }

    public List<Match> getMatchesForSemifinals() {
        return semifinalsMatches;
    }

    public Match getFinalMatch() {
        return finalsMatch;
    }

    @Override
    public List<Match> getMatches() {
        List<Match> allMatches = new ArrayList<>();
        allMatches.addAll(roundOfSixteenMatches);
        allMatches.addAll(quarterfinalsMatches);
        allMatches.addAll(semifinalsMatches);
        allMatches.add(finalsMatch);
        allMatches.add(thirdPlaceMatch);
        return Collections.unmodifiableList(allMatches);
    }

    public Team getFirstPlaceTeam() {
        return firstPlaceTeam;
    }

    public Team getSecondPlaceTeam() {
        return secondPlaceTeam;
    }

    public Team getThirdPlaceTeam() {
        return thirdPlaceTeam;
    }
}