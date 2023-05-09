package Backend;

import Backend.stage.*;

import java.util.ArrayList;
import java.util.List;

public class WorldCupSimulator {

    private List<Team> teams;
    private QualifyingStage qualifiers;
    private GroupStage roundRobbin;
    private KnockoutStage brackets;
    private static DataLoader dataLoader;

    public WorldCupSimulator(){
        dataLoader = new DataLoader();
        this.startProgram();
        qualifiers = new QualifyingStage(teams);
        roundRobbin = null;
        brackets = null;
    }
    private void startProgram() {
        teams = dataLoader.loadTeamData();
    }

    public List<Team> getTeams() {
        return teams;
    }
    public List<Match> stageMatches(int stage){ //1 = qualifier, 2 = groups, 3 = knockout
        switch (stage){
            case 1:
                qualifiers.arrangeMatches();
                qualifiers.calculateMatchResults();
                return qualifiers.getMatches();
            case 2:
                teams = qualifiers.qualifiedTeams();
                roundRobbin = new GroupStage(teams);
                roundRobbin.arrangeMatches();
                roundRobbin.calculateMatchResults();
                return roundRobbin.getMatches();
            case 3:
                teams = roundRobbin.qualifiedTeams();
                brackets = new KnockoutStage(teams);
                brackets.arrangeMatches();
                brackets.calculateMatchResults();
                return brackets.getMatches();
        }
        return null;
    }

    public static DataLoader getDataLoader() {
        return dataLoader;
    }
}