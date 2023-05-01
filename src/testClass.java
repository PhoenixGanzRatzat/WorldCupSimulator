import Backend.*;

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
        teams.add(new Team("Abed","ABC", Region.AFC, 1));
        teams.add(new Team("Abed","ABC", Region.AFC, 2));
        teams.add(new Team("Abed","ABC", Region.AFC, 3));
        teams.add(new Team("Abed","ABC", Region.AFC, 4));
        teams.add(new Team("Abed","ABC", Region.AFC, 5));
        teams.add(new Team("Abed","ABC", Region.AFC, 6));
        teams.add(new Team("Abed","ABC", Region.AFC, 7));
        teams.add(new Team("Abed","ABC", Region.AFC, 8));
        teams.add(new Team("Abed","ABC", Region.AFC, 1));
        teams.add(new Team("Abed","ABC", Region.AFC, 2));
        teams.add(new Team("Abed","ABC", Region.AFC, 3));
        teams.add(new Team("Abed","ABC", Region.AFC, 4));
        teams.add(new Team("Abed","ABC", Region.AFC, 5));
        teams.add(new Team("Abed","ABC", Region.AFC, 6));
        teams.add(new Team("Abed","ABC", Region.AFC, 7));
        teams.add(new Team("Abed","ABC", Region.AFC, 8));
        teams.add(new Team("Abed","ABC", Region.AFC, 1));
        teams.add(new Team("Abed","ABC", Region.AFC, 2));
        teams.add(new Team("Abed","ABC", Region.AFC, 3));
        teams.add(new Team("Abed","ABC", Region.AFC, 4));
        teams.add(new Team("Abed","ABC", Region.AFC, 5));
        teams.add(new Team("Abed","ABC", Region.AFC, 6));
        teams.add(new Team("Abed","ABC", Region.AFC, 7));
        teams.add(new Team("Abed","ABC", Region.AFC, 8));
        teams.add(new Team("Abed","ABC", Region.AFC, 1));
        teams.add(new Team("Abed","ABC", Region.AFC, 2));
        teams.add(new Team("Abed","ABC", Region.AFC, 3));
        teams.add(new Team("Abed","ABC", Region.AFC, 4));
        teams.add(new Team("Abed","ABC", Region.AFC, 5));
        teams.add(new Team("Abed","ABC", Region.AFC, 6));
        teams.add(new Team("Abed","ABC", Region.AFC, 7));
        teams.add(new Team("Abed","ABC", Region.AFC, 8));

        GroupStage gTest = new GroupStage(teams);
        gTest.arrangeMatches();
        gTest.calculateMatchResults();
        int count = 0;
        for(Match M:gTest.getMatches()){
            count++;
            System.out.println(count);
        }
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static void main(String[] args) {
        startProgram();
    }
}