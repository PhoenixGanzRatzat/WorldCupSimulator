import Backend.DataLoader;
import Backend.GroupStage;
import Backend.Region;
import Backend.Team;

import java.util.ArrayList;
import java.util.List;

//DO NOT PUSH NEW VERSIONS OF THIS CLASS
//DELETE CLASS BEFORE FINAL SUBMISSION

public class testClass {

    private static ArrayList<Team> teams;
    private static final DataLoader dataLoader = new DataLoader();

    private static void startProgram() {
        teams = new ArrayList<Team>();
        //teams = dataLoader.loadTeamData();
        //For testing
        teams.add(new Team("Abed","ABC", Region.UNNAMED_REGION, 1));
        teams.add(new Team("Abed","ABC", Region.UNNAMED_REGION, 5));
        teams.add(new Team("Abed","ABC", Region.UNNAMED_REGION, 2));
        GroupStage gTest = new GroupStage(teams);
        gTest.arrangeMatches();
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static void main(String[] args) {
        startProgram();
    }
}