package Backend.stage;

import Backend.Match;
import Backend.Team;
import Backend.exception.TeamListNotSizedProperlyException;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

    public KnockoutStage(List<Team> teams) {
        super(teams);
        roundOfSixteenMatches = new ArrayList<>();
        quarterfinalsMatches = new ArrayList<>();
        semifinalsMatches = new ArrayList<>();
        arrangeMatches();
    }

    @Override
    public void arrangeMatches() {
        if (isTeamListNotSizedProperly(getTeams()))
            throw new TeamListNotSizedProperlyException("Can't create matches from an oddly counted, or empty, list of teams.");
        roundOfSixteenMatches = createMatchesFromTeams(getTeams());
    }

    private boolean isTeamListNotSizedProperly(List<Team> teams) {
        return teams.isEmpty() || isNotEvenNumberOfTeams(teams);
    }

    private boolean isNotEvenNumberOfTeams(List<Team> teams) {
        return teams.size() % 2 != 0;
    }

    private List<Match> createMatchesFromTeams(List<Team> teams) {
        List<Match> matches = new ArrayList<>();
        List<LocalDate> matchDates = getMatchDatesFromNumberOfTeams(teams.size());
        for (int i = 0; i < teams.size(); i += 2) {
            LocalDate matchDate = teams.size() <= NUM_TEAMS_IN_SEMIFINALS ? matchDates.get(i / 2) : matchDates.get(i / 4);
            Match match = new Match(teams.get(i), teams.get(i + 1), matchDate);
            matches.add(match);
        }
        return matches;
    }

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

    private void simulateAllStageRounds() {
        simulateRoundOfSixteen();
        List<Team> lastMatchWinners = getWinningTeamsOfMatchResults(roundOfSixteenMatches);

        simulateQuarterfinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(quarterfinalsMatches);

        simulateSemifinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(semifinalsMatches);
        List<Team> lastMatchLosers = getLosingTeamsOfMatches(semifinalsMatches);
        thirdPlaceMatch = simulateThirdPlacePlayoffMatch(lastMatchLosers.get(0), lastMatchLosers.get(1));
        thirdPlaceTeam = thirdPlaceMatch.getWinningTeam();

        finalsMatch = simulateFinalsMatch(lastMatchWinners.get(0), lastMatchWinners.get(1));
        firstPlaceTeam = finalsMatch.getWinningTeam();
        secondPlaceTeam = finalsMatch.getLosingTeam();
    }

    private void simulateRoundOfSixteen() {
        roundOfSixteenMatches.forEach(Match::simulateMatchResult);
    }

    private void simulateQuarterfinals(List<Team> teams) {
        quarterfinalsMatches = createMatchesFromTeams(teams);
        quarterfinalsMatches.forEach(Match::simulateMatchResult);
    }

    private void simulateSemifinals(List<Team> teams) {
        semifinalsMatches = createMatchesFromTeams(teams);
        semifinalsMatches.forEach(Match::simulateMatchResult);
    }

    private Match simulateFinalsMatch(Team teamOne, Team teamTwo) {
        LocalDate matchDate = LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 15);
        Match match = new Match(teamOne, teamTwo, matchDate);
        match.simulateMatchResult();
        return match;
    }

    private Match simulateThirdPlacePlayoffMatch(Team teamOne, Team teamTwo) {
        LocalDate matchDate = LocalDate.of(WORLD_CUP_YEAR, Month.JULY.getValue(), 14);
        Match match = new Match(teamOne, teamTwo, matchDate);
        match.simulateMatchResult();
        return match;
    }

    private List<Team> getWinningTeamsOfMatchResults(List<Match> match) {
        return match.stream().map(Match::getWinningTeam).collect(Collectors.toList());
    }

    private List<Team> getLosingTeamsOfMatches(List<Match> matches) {
        return matches.stream().map(Match::getLosingTeam).collect(Collectors.toList());
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

    public Match getFinalsMatch() {
        return finalsMatch;
    }

    @Override
    public List<Match> getMatches() {
        super.getMatches().addAll(quarterfinalsMatches);
        super.getMatches().addAll(semifinalsMatches);
        super.getMatches().add(finalsMatch);
        super.getMatches().add(thirdPlaceMatch);
        return Collections.unmodifiableList(super.getMatches());
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