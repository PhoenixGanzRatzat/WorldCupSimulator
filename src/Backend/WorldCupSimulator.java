package Backend;

import Backend.stage.*;

import java.util.ArrayList;
import java.util.List;

public class WorldCupSimulator {

    private static ArrayList<Team> teams;
    private static final DataLoader dataLoader = new DataLoader();
    private QualifyingStage qualifiers;
    private GroupStage roundRobbin;
    private KnockoutStage brackets;
    public WorldCupSimulator(){
        teams = DataLoader.loadTeamData();
        qualifiers = new QualifyingStage(teams);
        this.stageMatches(1);
        roundRobbin = new GroupStage(teams);
        teams = roundRobbin.qualified();
        this.stageMatches(2);
        brackets = new KnockoutStage(teams);
        this.stageMatches(3);
    }
    private static void startProgram() {
        teams = dataLoader.loadTeamData();
    }

    public static ArrayList<Team> getTeams() {
        return teams;
    }
    public List<Match> stageMatches(int stage){ //1 = qualifier, 2 = groups, 3 = knockout
        switch (stage){
            case 1:
                qualifiers.arrangeMatches();
                qualifiers.calculateMatchResults();
                return qualifiers.getMatches();
            case 2:
                roundRobbin.arrangeMatches();
                roundRobbin.calculateMatchResults();
                return roundRobbin.getMatches();
            case 3:
                brackets.arrangeMatches();
                brackets.calculateMatchResults();
                return brackets.getMatches();
        }
        return null;
    }

    public static void main(String[] args) {
        startProgram();
    }
}