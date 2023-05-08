package Backend.stage;
import Backend.Match;
import Backend.Team;

import java.time.LocalDate;
import java.util.*;

/**
 * Group Stage of the tournament. Receive teams from qualifying stage and sorts them into 8 groups
 * which then perform round-robin tournaments. The two teams from each group with the highest point
 * values move on to knockout stage. Creates all match objects for each group and simulates them.
 */
public class GroupStage extends Stage{
    /* Key: group number, Value: teams in that group */
    private HashMap<Integer, List<Team>> groups;
    /* All teams that made it to the group stage */
    private List<Team> teams;
    /* All matches played in all groups */
    private List<Match> matches;
    /* Key: group number, Value: matches played in that group */
    private HashMap<Integer, ArrayList<Match>> groupMatches;
    /* 2 teams from each group with the highest point value move on to knockout stage */
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

        this.teams = getTeams();
        this.matches = getMatches(); // init with empty list

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
    }

    /**
     * Sort all teams into 8 groups and create Matches for round-robin tournaments
     */
    @Override
    public void arrangeMatches() {
        // index of next Team in this.teams to add to a group
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
        // TODO: start/seed date for group stage
        LocalDate seed = LocalDate.of(2018, 5, 14);
        // for each group
        for (Integer groupNumber : groups.keySet()) {

            // create all matches in each group, each team vs each team once.
            // team (a) plays each team indexed after (a)
            for(int a = 0; a < groups.get(groupNumber).size() - 1; a++) { // [g.size()-1] > all matches are created before last team becomes team (a)
                // team (b) starts at the index after team (a)
                for(int b = (a + 1); b < groups.get(groupNumber).size(); b++) {
                    // match
                    // TODO: match date algorithm - assume every Match in the GroupStage is played on separate consecutive days
                    Match nextMatch = new Match(groups.get(groupNumber).get(a), // team a
                            groups.get(groupNumber).get(b), // team b
                            seed.plusDays(1));
                    // Collect all matches in Group Stage of tournament
                    matches.add(nextMatch);
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
            for (Team team : groups.get(groupNumber)) {
                teamPoints.put(team, team.getMostRecentScore());
                /*Team team1 = match.getTeam1();
                Team team2 = match.getTeam2();
                int score1 = match.getTeam1Score();
                int score2 = match.getTeam2Score();

                // give points to teams based on wins and draws
                if (score1 > score2) {
                    teamPoints.put(team1, (teamPoints.get(team1) + 3));
                } else if (score1 < score2) {
                    teamPoints.put(team2, (teamPoints.get(team2) + 3));
                } else {
                    teamPoints.put(team1, (teamPoints.get(team1) + 1));
                    teamPoints.put(team2, (teamPoints.get(team2) + 1));
                }*/
            }
            // sort teams by points in descending order(highest to lowest)
            // TODO: BUG: groupPanel and groupStage can have different group stage winners if 2nd & 3rd place have the same point values
            List<Map.Entry<Team, Integer>> sortedTeams = new ArrayList<>(teamPoints.entrySet());
            sortedTeams.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            // Collect the two highest point value teams
            teamsMovingOntoKnockout.add(sortedTeams.get(0).getKey());
            teamsMovingOntoKnockout.add(sortedTeams.get(1).getKey());
        }
    }

    /**
     * Run the simulation method for each Match
     */
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