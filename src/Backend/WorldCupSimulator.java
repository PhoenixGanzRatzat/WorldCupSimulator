package Backend;

import Backend.DataLoader;
import Backend.Team;

import java.util.List;

public class WorldCupSimulator {

    private static List<Team> teams;
    private static final DataLoader dataLoader = new DataLoader();

    private static void startProgram() {
        teams = dataLoader.loadTeamData();
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static void main(String[] args) {
        startProgram();
    }
}