package Backend;

import Backend.stage.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class WorldCupSimulator {

    private List<Team> teams;
    private final DataLoader dataLoader = new DataLoader();
    private QualifyingStage qualifiers;
    private GroupStage roundRobin;
    private KnockoutStage brackets;
    public WorldCupSimulator(){
        this.startProgram();
        qualifiers = new QualifyingStage(teams);
        roundRobin = null;
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
                //qualifiers.calculateMatchResults();
                return qualifiers.getMatches();
            case 2:
                teams = qualifiers.qualifiedTeams();
                roundRobin = new GroupStage(teams);
                roundRobin.arrangeMatches();
                roundRobin.calculateMatchResults();
                return roundRobin.getMatches();
            case 3:
                teams = roundRobin.qualifiedTeams();
                brackets = new KnockoutStage(teams);
                brackets.arrangeMatches();
                brackets.calculateMatchResults();
                return brackets.getMatches();
        }
        return null;
    }

    public static void main(String[] args) {
        WorldCupSimulator gameSim = new WorldCupSimulator();
        System.out.println(gameSim.stageMatches(1).stream().filter(Match -> Match.getTeam1().getPointsMap().keySet().stream()
        .anyMatch(LocalDate -> LocalDate.isEqual(java.time.LocalDate.of(2023, 5, 8)))).sorted(new Comparator<Match>() {
            @Override
            public int compare(Match o1, Match o2) {
                int result = 0;
                if (o1.getMatchDate().isBefore(o2.getMatchDate())) result = -1;
                else if (o1.getMatchDate().isAfter(o2.getMatchDate())) result = 1;
                return result;
            }
        }).map(Match -> Match.getTeam1().getPoints(LocalDate.of(2023, 5, 8)) + " " + Match.getTeam1().getRegion() + "\n").collect(Collectors.toSet()));
        System.out.println(gameSim.stageMatches(1).size());
    }
}