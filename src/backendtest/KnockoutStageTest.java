package backendtest;

import Backend.DataLoader;
import Backend.stage.KnockoutStage;
import Backend.Team;

import java.util.List;

public class KnockoutStageTest {

    private final List<Team> teams;

    KnockoutStageTest() {
        teams = DataLoader.loadTeamData().subList(0, 16);
        createKnockoutStage();
        simulateRounds();
    }

    private void createKnockoutStage() {
        new KnockoutStage(teams);
    }

    private void simulateRounds() {
        KnockoutStage knockoutStage = new KnockoutStage(teams);
        knockoutStage.calculateMatchResults();
        System.out.println("knockoutStage.getFirstPlaceTeam() = " + knockoutStage.getFirstPlaceTeam());
        System.out.println("knockoutStage.getSecondPlaceTeam() = " + knockoutStage.getSecondPlaceTeam());
        System.out.println("knockoutStage.getThirdPlaceTeam() = " + knockoutStage.getThirdPlaceTeam());
    }

    public static void main(String[] args) {
        new KnockoutStageTest();
    }
}
