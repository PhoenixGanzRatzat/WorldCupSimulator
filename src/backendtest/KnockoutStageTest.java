package backendtest;

import Backend.DataLoader;
import Backend.KnockoutStage;
import Backend.Team;

import java.util.List;

public class KnockoutStageTest {

    private final List<Team> teams;

    KnockoutStageTest() {
        teams = DataLoader.loadTeamData();
        createKnockoutStage();
        simulateRounds();
    }

    private void createKnockoutStage() {
        new KnockoutStage(teams);
    }

    private void simulateRounds() {
        KnockoutStage knockoutStage = new KnockoutStage(teams.subList(0, 8));
        knockoutStage.calculateMatchResults();
        System.out.println("knockoutStage.getFirstPlaceTeam() = " + knockoutStage.getFirstPlaceTeam());
        System.out.println("knockoutStage.getSecondPlaceTeam() = " + knockoutStage.getSecondPlaceTeam());
        System.out.println("knockoutStage.getThirdPlaceTeam() = " + knockoutStage.getThirdPlaceTeam());
    }

    public static void main(String[] args) {
        new KnockoutStageTest();
    }
}