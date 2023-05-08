package backendtest;

import Backend.DataLoader;
import Backend.Match;
import Backend.stage.KnockoutStage;
import Backend.Team;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KnockoutStageTest {

    private static final int NUM_OF_TEAMS_IN_KNOCKOUT_STAGE = 16;
    private final List<Team> teams;

    private final KnockoutStage knockoutStage;

    KnockoutStageTest() {
        teams = DataLoader.loadTeamData().subList(0, NUM_OF_TEAMS_IN_KNOCKOUT_STAGE);
        knockoutStage = new KnockoutStage(teams);
        arrangeMatches();
        simulateRounds();
        getMatchesForRoundOfSixteen();
        getMatchesForQuarterfinals();
        getMatchesForSemifinals();
        getFinalMatch();
        getMatches();
    }

    private void arrangeMatches() {
        knockoutStage.arrangeMatches();
        System.out.println();
    }

    private void simulateRounds() {
        knockoutStage.calculateMatchResults();
        System.out.println("knockoutStage.getFirstPlaceTeam() = " + knockoutStage.getFirstPlaceTeam());
        System.out.println("knockoutStage.getSecondPlaceTeam() = " + knockoutStage.getSecondPlaceTeam());
        System.out.println("knockoutStage.getThirdPlaceTeam() = " + knockoutStage.getThirdPlaceTeam());

        knockoutStage.getMatches().forEach(match -> System.out.println("match = " + match));
        System.out.println();
    }

    private void getMatches() {
        System.out.println("Printing matches from knockoutStage.getMatches()");
        printMatches(knockoutStage.getMatches());
        System.out.println();
    }

    private void getMatchesForRoundOfSixteen() {
        System.out.println("Printing matches from knockoutStage.getMatchesForRoundOfSixteen()");
        printMatches(knockoutStage.getMatchesForRoundOfSixteen());
        System.out.println();
    }

    private void getMatchesForQuarterfinals() {
        System.out.println("Printing matches from knockoutStage.getMatchesForQuarterfinals()");
        printMatches(knockoutStage.getMatchesForQuarterfinals());
        System.out.println();
    }

    private void getMatchesForSemifinals() {
        System.out.println("Printing matches from knockoutStage.getMatchesForSemifinals()");
        printMatches(knockoutStage.getMatchesForSemifinals());
        System.out.println();
    }

    private void getFinalMatch() {
        System.out.println("Printing matches from knockoutStage.getFinalsMatch()");
        printMatches(Collections.singletonList(knockoutStage.getFinalMatch()));
        System.out.println();
    }

    private void printMatches(Collection<Match> matches) {
        System.out.println("matches.size() = " + matches.size());
        for (Match match : matches) {
            System.out.println("match = " + match);
        }
    }

    public static void main(String[] args) {
        new KnockoutStageTest();
    }
}
