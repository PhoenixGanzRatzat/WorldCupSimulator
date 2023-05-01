package Backend;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KnockoutStage extends Stage {

    private List<MatchResult> roundOfSixteenMatchResults;
    private List<MatchResult> quarterfinalsMatchResults;
    private List<MatchResult> semifinalsMatchResults;
    private MatchResult finalsMatchResult;
    private Team firstPlaceTeam;
    private Team secondPlaceTeam;
    private Team thirdPlaceTeam;

    public KnockoutStage(List<Team> teams) {
        super(teams);
        roundOfSixteenMatchResults = new ArrayList<>();
        quarterfinalsMatchResults = new ArrayList<>();
        semifinalsMatchResults = new ArrayList<>();
    }

    @Override
    public void arrangeMatches() {

    }

    private List<Match> createMatchesFromTeams(List<Team> teams) {
        List<Match> matches = new ArrayList<>();
        if (teams.size() <= 1)
            return Collections.emptyList();
        if (teams.size() % 2 != 0)
            teams.remove(teams.size() - 1);
        for (int i = 0; i < teams.size(); i += 2) {
            Match match = new Match(teams.get(i), teams.get(i + 1));
            matches.add(match);
        }
        return matches;
    }

    @Override
    public void calculateMatchResults() {
        simulateRounds();
    }

    private void simulateRounds() {
        roundOfSixteenMatchResults = simulateRoundOfSixteen();
        List<Team> lastMatchWinners = getWinningTeamsOfMatchResults(roundOfSixteenMatchResults);

        quarterfinalsMatchResults = simulateQuarterfinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(quarterfinalsMatchResults);

        semifinalsMatchResults = simulateSemifinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(semifinalsMatchResults);
        List<Team> lastMatchLosers = getLosingTeamsOfMatchResults(semifinalsMatchResults);
        MatchResult thirdPlacePlayoffMatchResult = simulateMatch(lastMatchLosers.get(0), lastMatchLosers.get(1), MatchType.THIRD_PLACE_PLAYOFF);
        thirdPlaceTeam = thirdPlacePlayoffMatchResult.getWinningTeam();

        finalsMatchResult = simulateMatch(lastMatchWinners.get(0), lastMatchWinners.get(1), MatchType.FINALS);
        firstPlaceTeam = finalsMatchResult.getWinningTeam();
        secondPlaceTeam = finalsMatchResult.getLosingTeam();
    }

    private List<Team> getWinningTeamsOfMatchResults(List<MatchResult> matchResults) {
        return matchResults.stream().map(MatchResult::getWinningTeam).collect(Collectors.toList());
    }

    private List<Team> getLosingTeamsOfMatchResults(List<MatchResult> matchResults) {
        return matchResults.stream().map(MatchResult::getLosingTeam).collect(Collectors.toList());
    }

    private List<MatchResult> simulateRoundOfSixteen() {
        List<Match> roundOfSixteenMatches = createMatchesFromTeams(getTeams());
        Stream<MatchResult> matchResultsStream = roundOfSixteenMatches.stream().map(Match::simulateRoundOfSixteenMatch);
        return matchResultsStream.collect(Collectors.toList());
    }

    private List<MatchResult> simulateQuarterfinals(List<Team> teams) {
        List<Match> quarterfinalsMatches = createMatchesFromTeams(teams);
        Stream<MatchResult> matchResultsStream = quarterfinalsMatches.stream().map(Match::simulateQuarterfinalsMatch);
        return matchResultsStream.collect(Collectors.toList());
    }

    private List<MatchResult> simulateSemifinals(List<Team> teams) {
        List<Match> semifinalsMatches = createMatchesFromTeams(teams);
        Stream<MatchResult> matchResultsStream = semifinalsMatches.stream().map(Match::simulateSemifinalsMatch);
        return matchResultsStream.collect(Collectors.toList());
    }

    private MatchResult simulateMatch(Team teamOne, Team teamTwo, MatchType matchType) {
        Match match = new Match(teamOne, teamTwo);
        return match.simulateMatchResult(matchType);
    }

    public List<MatchResult> getMatchResultsForRoundOfSixteen() {
        return roundOfSixteenMatchResults;
    }

    public List<MatchResult> getQuarterfinalsMatchResults() {
        return quarterfinalsMatchResults;
    }

    public List<MatchResult> getSemifinalsMatchResults() {
        return semifinalsMatchResults;
    }

    public MatchResult getFinalsMatchResult() {
        return finalsMatchResult;
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