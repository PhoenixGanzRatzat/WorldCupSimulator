package Backend;

import java.util.*;

public class KnockoutStage extends Stage {

    private static final int MATCH_COUNT_FOR_QUARTERFINALS = 4;
    private static final int MATCH_COUNT_FOR_SEMIFINALS = 2;
    private static final int MATCH_COUNT_FOR_FINALS = 1;
    private List<Match> matchesForRoundOfSixteen;
    private List<Match> matchesForQuarterfinals;
    private List<Match> matchesForSemifinals;
    private Team firstPlaceTeam;
    private Team secondPlaceTeam;
    private Team thirdPlaceTeam;

    public KnockoutStage(List<Team> teams) {
        super(teams);
        matchesForRoundOfSixteen = createMatchesFromTeams(teams);
        matchesForQuarterfinals = new ArrayList<>();
        matchesForSemifinals = new ArrayList<>();
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
        simulateRounds(matchesForRoundOfSixteen);
    }

    private void simulateRounds(List<Match> matches) {
        MatchType round;
        switch (matches.size()) {
            case MATCH_COUNT_FOR_QUARTERFINALS:
                round = MatchType.QUARTERFINALS;
                break;
            case MATCH_COUNT_FOR_SEMIFINALS:
                round = MatchType.SEMIFINALS;
                break;
            case MATCH_COUNT_FOR_FINALS:
                round = MatchType.FINALS;
                break;
            default:
                round = MatchType.ROUND_OF_SIXTEEN;
                break;
        }

        if (matches.size() == MATCH_COUNT_FOR_SEMIFINALS) {
            matchesForSemifinals.addAll(matches);
            Match firstMatch = matches.get(0);
            Match secondMatch = matches.get(1);
            firstMatch.simulateMatchResult(MatchType.SEMIFINALS);
            secondMatch.simulateMatchResult(MatchType.SEMIFINALS);
            Match finalMatch = new Match(firstMatch.getWinningTeam(), secondMatch.getWinningTeam());
            finalMatch.simulateMatchResult(MatchType.FINALS);
            firstPlaceTeam = finalMatch.getWinningTeam();
            secondPlaceTeam = finalMatch.getLosingTeam();
            Match thirdPlaceMatch = new Match(firstMatch.getLosingTeam(), secondMatch.getLosingTeam());
            thirdPlaceMatch.simulateMatchResult(MatchType.THIRD_PLACE_PLAYOFF);
            thirdPlaceTeam = thirdPlaceMatch.getWinningTeam();
            return;
        }

        if (matches.size() == MATCH_COUNT_FOR_QUARTERFINALS) {
            matchesForQuarterfinals.addAll(matches);
        }

        List<Team> winningTeams = new ArrayList<>();
        for (Match match : matches) {
            match.simulateMatchResult(round);
            winningTeams.add(match.getWinningTeam());
        }
        List<Match> matchesForNextRound = createMatchesFromTeams(winningTeams);
        if (matchesForNextRound.isEmpty())
            return;
        simulateRounds(matchesForNextRound);
    }

    public List<Match> getMatchesForRoundOfSixteen() {
        return matchesForRoundOfSixteen;
    }

    public List<Match> getMatchesForQuarterfinals() {
        return matchesForQuarterfinals;
    }

    public List<Match> getMatchesForSemifinals() {
        return matchesForSemifinals;
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