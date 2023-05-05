package Backend;

import Backend.stage.*;

import java.util.ArrayList;
import java.util.List;

public class WorldCupSimulator {
    private static List<Team> teams;
    private QualifyingStage qualifiers;
    private GroupStage roundRobbin;
    private KnockoutStage brackets;
    public WorldCupSimulator(){
        // load all teams
        // TODO: handle exceptions in class - check
        teams = DataLoader.loadTeamData();

        // Qualifying stage
        qualifiers = new QualifyingStage((ArrayList<Team>) teams);
        // Create and simulate all matches in stage
        this.simulateStages(1);

        // Group stage
        //roundRobbin = new GroupStage(qualifiers.getNextStageTeams()); // TODO: actual
        roundRobbin = new GroupStage(teamsMovingOnToGroupStage()); // TODO: tester
        // Create and simulate all matches in stage
        this.simulateStages(2);

        // knockout stage
        //brackets = new KnockoutStage(roundRobbin.getNextStageTeams());// TODO: actual
        brackets = new KnockoutStage(teamsMovingOnToKnockoutStage()); // TODO: tester
        // Create and simulate all matches in stage
        this.simulateStages(3);
        // End of simulations
    }

    private List<Team> teamsMovingOnToKnockoutStage() {
        List<Team> sampleTeams = new ArrayList<Team>();
        sampleTeams.add( new Team("United States", "USA", null, 0));
        sampleTeams.add( new Team("Canada", "CAN", null, 0));
        sampleTeams.add( new Team("France", "FRA", null, 0));
        sampleTeams.add( new Team("Iran", "IRN", null, 0));
        sampleTeams.add( new Team("Netherlands", "NED", null, 0));
        sampleTeams.add( new Team("Ecuador", "ECU", null, 0));
        sampleTeams.add( new Team("Croatia", "CRC", null, 0));
        sampleTeams.add( new Team("Japan", "JPN", null, 0));
        sampleTeams.add( new Team("South Korea", "KOR", null, 0));
        sampleTeams.add( new Team("China", "CHN",null, 0));
        sampleTeams.add( new Team("Oman", "OMA", null, 0));
        sampleTeams.add( new Team("Lebanon", "LBN",null, 0));
        sampleTeams.add( new Team("Pakistan", "PAK", null, 0));
        sampleTeams.add( new Team("Macau", "MAC",null, 0));
        sampleTeams.add( new Team("El Salvador", "SLV", null, 0));
        sampleTeams.add( new Team("Togo", "TOG",null, 0));
        return sampleTeams;
    }

    private List<Team> teamsMovingOnToGroupStage() {
        List<Team> sampleTeams = new ArrayList<Team>();
        sampleTeams.add( new Team("United States", "USA", null, 0));
        sampleTeams.add( new Team("Canada", "CAN", null, 0));
        sampleTeams.add( new Team("Germany", "GER", null, 0));
        sampleTeams.add( new Team("Sudan", "SDN", null, 0));

        sampleTeams.add( new Team("France", "FRA", null, 0));
        sampleTeams.add( new Team("Iran", "IRN", null, 0));
        sampleTeams.add( new Team("Wales", "WAL", null, 0));
        sampleTeams.add( new Team("England", "ENG", null, 0));

        sampleTeams.add( new Team("Netherlands", "NED", null, 0));
        sampleTeams.add( new Team("Ecuador", "ECU", null, 0));
        sampleTeams.add( new Team("Qatar", "QAT", null, 0));
        sampleTeams.add( new Team("Australia", "AUS", null, 0));

        sampleTeams.add( new Team("Croatia", "CRC", null, 0));
        sampleTeams.add( new Team("Japan", "JPN", null, 0));
        sampleTeams.add( new Team("Denmark", "DEN", null, 0));
        sampleTeams.add( new Team("Tunisia", "TUN", null, 0));

        sampleTeams.add( new Team("South Korea", "KOR", null, 0));
        sampleTeams.add( new Team("China", "CHN",null, 0));
        sampleTeams.add( new Team("Iraq", "IRQ", null, 0));
        sampleTeams.add( new Team("India", "IND", null, 0));

        sampleTeams.add( new Team("Oman", "OMA", null, 0));
        sampleTeams.add( new Team("Lebanon", "LBN",null, 0));
        sampleTeams.add( new Team("Mexico", "MEX", null, 0));
        sampleTeams.add( new Team("Dominica", "DMA", null, 0));

        sampleTeams.add( new Team("Pakistan", "PAK", null, 0));
        sampleTeams.add( new Team("Macau", "MAC",null, 0));
        sampleTeams.add( new Team("Libya", "LBY", null, 0));
        sampleTeams.add( new Team("Namibia", "NAM", null, 0));

        sampleTeams.add( new Team("El Salvador", "SLV", null, 0));
        sampleTeams.add( new Team("Togo", "TOG",null, 0));
        sampleTeams.add( new Team("Bahamas", "BAH", null, 0));
        sampleTeams.add( new Team("Bermuda", "BER", null, 0));

        return sampleTeams;
    }

    private static void startProgram() {
        teams = DataLoader.loadTeamData();
    }

    public static List<Team> getTeams() {
        return teams;
    }
    public void simulateStages(int stage){ //1 = qualifier, 2 = groups, 3 = knockout
        switch (stage){
            case 1:
                qualifiers.arrangeMatches();
                qualifiers.calculateMatchResults();
            case 2:
                roundRobbin.arrangeMatches();
                roundRobbin.calculateMatchResults();
            case 3:
                brackets.arrangeMatches();
                brackets.calculateMatchResults();
        }
    }

    public static void main(String[] args) {
        startProgram();
    }
}