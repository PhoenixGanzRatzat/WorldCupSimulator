package Backend;

import Backend.DataLoader;
import Backend.Team;

import java.util.ArrayList;
import java.util.List;

public class WorldCupSimulator {

    private static List<Team> teams;
    private static final DataLoader dataLoader = new DataLoader();
    private QualifyingStage qualifiers;
    private GroupStage roundRobbin;
    private KnockoutStage brackets;

    private static void startProgram() {
        teams = dataLoader.loadTeamData();
    }

    public static List<Team> getTeams() {
        return teams;
    }
    public ArrayList<Match> stageMatches(int stage){ //1 = qualifier, 2 = groups, 3 = knockout
        switch (stage){
            case 1:
                return qualifiers.getMatches();
            case 2:
                return roundRobbin.getMatches();
            case 3:
                return brackets.getMatches();
        }
        return null;
    }

    public static void main(String[] args) {
        startProgram();
    }
}