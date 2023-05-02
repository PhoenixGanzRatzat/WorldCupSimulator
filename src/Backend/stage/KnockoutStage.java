package Backend.stage;

import Backend.Match;
import Backend.MatchType;
import Backend.Team;
import Backend.exception.TeamListNotSizedProperlyException;

import java.util.*;
import java.util.stream.Collectors;

public class KnockoutStage extends Stage {

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

    private List<Match> createMatchesFromTeams(List<Team> teams) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < teams.size(); i += 2) {
            Match match = new Match(teams.get(i), teams.get(i + 1));
            matches.add(match);
        }
        return matches;
    }

    private boolean isTeamListNotSizedProperly(List<Team> teams) {
        return teams.isEmpty() || isNotEvenNumberOfTeams(teams);
    }

    private boolean isNotEvenNumberOfTeams(List<Team> teams) {
        return teams.size() % 2 != 0;
    }

    @Override
    public void calculateMatchResults() {
        simulateRounds();
    }

    private void simulateRounds() {
        simulateRoundOfSixteen();
        List<Team> lastMatchWinners = getWinningTeamsOfMatchResults(roundOfSixteenMatches);

        simulateQuarterfinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(quarterfinalsMatches);

        simulateSemifinals(lastMatchWinners);
        lastMatchWinners = getWinningTeamsOfMatchResults(semifinalsMatches);
        List<Team> lastMatchLosers = getLosingTeamsOfMatches(semifinalsMatches);
        thirdPlaceMatch = simulateMatch(lastMatchLosers.get(0), lastMatchLosers.get(1), MatchType.THIRD_PLACE_PLAYOFF);
        thirdPlaceTeam = thirdPlaceMatch.getWinningTeam();

        finalsMatch = simulateMatch(lastMatchWinners.get(0), lastMatchWinners.get(1), MatchType.FINALS);
        firstPlaceTeam = finalsMatch.getWinningTeam();
        secondPlaceTeam = finalsMatch.getLosingTeam();
    }

    private List<Team> getWinningTeamsOfMatchResults(List<Match> match) {
        return match.stream().map(Match::getWinningTeam).collect(Collectors.toList());
    }

    private List<Team> getLosingTeamsOfMatches(List<Match> matches) {
        return matches.stream().map(Match::getLosingTeam).collect(Collectors.toList());
    }

    private void simulateRoundOfSixteen() {
        roundOfSixteenMatches.forEach(match -> match.simulateMatchResult(MatchType.ROUND_OF_SIXTEEN));
    }

    private void simulateQuarterfinals(List<Team> teams) {
        quarterfinalsMatches = createMatchesFromTeams(teams);
        quarterfinalsMatches.forEach(match -> match.simulateMatchResult(MatchType.QUARTERFINALS));
    }

    private void simulateSemifinals(List<Team> teams) {
        semifinalsMatches = createMatchesFromTeams(teams);
        semifinalsMatches.forEach(match -> match.simulateMatchResult(MatchType.SEMIFINALS));
    }

    private Match simulateMatch(Team teamOne, Team teamTwo, MatchType matchType) {
        Match match = new Match(teamOne, teamTwo);
        match.simulateMatchResult(matchType);
        return match;
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

    public List<Match> getAllMatches() {
        List<Match> allMatches = new ArrayList<>(roundOfSixteenMatches);
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