package Backend.stage;
import Backend.Match;
import Backend.Team;

import java.time.LocalDate;
import java.util.*;

public class GroupStage extends Stage{
    private HashMap<Integer, List<Team>> groups;
    private List<Team> teams;
    private List<Match> matches;
    private HashMap<Integer, ArrayList<Match>> groupMatches;
    private List<Team> teamsMovingOntoKnockout;

    public GroupStage(List<Team> teams) {
        super(teams);
        this.groups = new HashMap<>();
        groups.put(0, new ArrayList<>());
        groups.put(1, new ArrayList<>());
        groups.put(2, new ArrayList<>());
        groups.put(3, new ArrayList<>());
        groups.put(4, new ArrayList<>());
        groups.put(5, new ArrayList<>());
        groups.put(6, new ArrayList<>());
        groups.put(7, new ArrayList<>());

        groupMatches = new HashMap<>();
        groupMatches.put(0, new ArrayList<>());
        groupMatches.put(1, new ArrayList<>());
        groupMatches.put(2, new ArrayList<>());
        groupMatches.put(3, new ArrayList<>());
        groupMatches.put(4, new ArrayList<>());
        groupMatches.put(5, new ArrayList<>());
        groupMatches.put(6, new ArrayList<>());
        groupMatches.put(7, new ArrayList<>());

        teamsMovingOntoKnockout = new ArrayList<>();
        this.teams = getTeams();
        this.matches = getMatches(); // always returns empty list
    }

    /**
     * Sort all teams into 8 groups
     *
     */
    @Override
    public void arrangeMatches() {
        // create groups
        // index of next Team to add to a group
        int teamIndex = 0;
        // for every team
        while(teamIndex < teams.size()) {
            // Cycle through each group adding the next team until every team is in 1 of the 8 groups
            for (Integer groupNum : groups.keySet()) {
                // add next team
                groups.get(groupNum).add(teams.get(teamIndex));
                teamIndex++;
            }
        }

        // create all matches in each group, each team vs each team once.
        // for each group
        for (Integer groupNumber : groups.keySet()) {
            // team (a) plays each team indexed after (a)
            for(int a = 0; a < groups.get(groupNumber).size() - 1; a++) { // [g.size()-1] > all matches created before last team becomes team (a)
                // team (b) starts at the index after team (a)
                for(int b = (a + 1); b < groups.get(groupNumber).size(); b++) {
                    // match
                    // TODO: start/seed date for group stage
                    LocalDate seed = LocalDate.of(2020, 1, 1);
                    // TODO: match date algorithm - assume every Match in the GroupStage is played on separate consecutive days
                    Match nextMatch = new Match(groups.get(groupNumber).get(a), // team a
                            groups.get(groupNumber).get(b), // team b
                            seed.plusDays(1));
                    matches.add(nextMatch); // list of all matches in stage
                    // Associate this match with this group
                    groupMatches.get(groupNumber).add(nextMatch); // Map of all matches by group
                }
            }
        }

    }

    /**
     * Determine which teams from each group that are moving on to the knockout stage
     */
    public void determineGroupWinners() {
        for(Integer groupNumber : groups.keySet()) {
            HashMap<Team, Integer> teamPoints = new HashMap<Team, Integer>();

            // calculate points for each team in each group
            for (Match match : groupMatches.get(groupNumber)) {
                Team team1 = match.getTeam1();
                Team team2 = match.getTeam2();
                int score1 = match.getTeam1Score();
                int score2 = match.getTeam2Score();

                // init teams if first time
                if(!(teamPoints.containsKey(team1))) {
                    teamPoints.put(team1, 0);
                }
                if(!(teamPoints.containsKey(team2))) {
                    teamPoints.put(team2, 0);
                }

                if (score1 > score2) {
                    teamPoints.put(team1, (teamPoints.get(team1) + 3));
                } else if (score1 < score2) {
                    teamPoints.put(team2, (teamPoints.get(team2) + 3));
                } else {
                    teamPoints.put(team1, (teamPoints.get(team1) + 1));
                    teamPoints.put(team2, (teamPoints.get(team2) + 1));
                }

            }
            // sort teams by points in descending order
            List<Map.Entry<Team, Integer>> sortedTeams = new ArrayList<>(teamPoints.entrySet());
            sortedTeams.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            // extract first two teams
            teamsMovingOntoKnockout.add(sortedTeams.get(0).getKey());
            teamsMovingOntoKnockout.add(sortedTeams.get(1).getKey());
        }
    }

    @Override
    public void calculateMatchResults(){
        for(Match m:matches){
            m.simulateMatchResult();
        }
        determineGroupWinners();
    }

    /**
     * @return List<Team> that won their group stage and are moving on to knockout stage
     */
    public List<Team> qualifiedTeams() {
        return teamsMovingOntoKnockout;
    }
    public HashMap<Integer, List<Team>> getGroups(){
        return groups;
    }

}