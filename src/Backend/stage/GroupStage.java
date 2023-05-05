package Backend.stage;
import Backend.Match;
import Backend.Team;

import java.time.LocalDate;
import java.util.*;

public class GroupStage extends Stage{
    List<List<Team>> groups;
    private List<Team> teams;
    private List<Match> matches;
    private HashMap<Integer, ArrayList<Match>> groupMatches;
    private List<Team> teamsMovingOntoKnockout;
    public GroupStage(List<Team> teams) {
        super(teams);
        this.groups = new ArrayList<>(8);
        groupMatches = new HashMap<>();
        teamsMovingOntoKnockout = new ArrayList<>();
        this.teams = getTeams();
        this.matches = getMatches();
    }

    /**
     * Sort all teams into 8 groups
     *
     */
    @Override
    public void arrangeMatches() {
        // create groups
        // for every team
        for (int teamCount = 0; teamCount < teams.size(); teamCount--) {
            // index of next Team to add to a group
            int teamIndex = 0;
            // Cycle through each group adding the next team until every team is in 1 of the 8 groups
            for (List<Team> group : groups) {
                // add next team
                group.add(teams.get(teamIndex));
                teamIndex++;
            }
        }

        // create all matches in each group, each team vs each team once.
        // for each group
        for (List<Team> group : groups){
            // team (a) plays each team indexed after (a)
            for(int a = 0; a < group.size() - 1; a++) { // [g.size()-1] > all matches created before last team becomes team (a)
                // team (b) starts at the index after team (a)
                for(int b = (a + 1); b < group.size(); b++) {
                    // match
                    // TODO: start/seed date for group stage
                    LocalDate seed = LocalDate.of(2020, 1, 1);
                    // TODO: match date algorithm - assume every Match in the GroupStage is played on separate consecutive days
                    Match nextMatch = new Match(group.get(a), group.get(b), seed.plusDays(1));
                    matches.add(nextMatch);
                    // Associate this match with this group
                    groupMatches.get(groups.indexOf(group)).add(nextMatch);
                }
            }
        }

    }

    /**
     * Determine which teams from each group that are moving on to the knockout stage
     */
    public void determineGroupWinners() {
        for(List<Team> group : groups) {
            HashMap<Team, Integer> teamPoints = new HashMap<Team, Integer>();
            int groupNumber = groups.indexOf(group);

            // calculate points for each team in each group
            for (Match match : groupMatches.get(groupNumber)) {
                Team team1 = match.getTeamOne();
                Team team2 = match.getTeamTwo();
                int score1 = match.getTeamOneScore();
                int score2 = match.getTeamTwoScore();

                if (score1 > score2) {
                    teamPoints.put(team1, (teamPoints.get(team1) + 3));
                } else if (score1 < score2) {
                    teamPoints.put(team2, (teamPoints.get(team2) + 3));
                } else {
                    teamPoints.put(team1, (teamPoints.get(team1) + 1));
                    teamPoints.put(team2, (teamPoints.get(team2) + 1));
                }

                // sort teams by points in descending order
                List<Map.Entry<Team, Integer>> sortedTeams = new ArrayList<>(teamPoints.entrySet());
                Collections.sort(sortedTeams, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

                // extract first two teams
                teamsMovingOntoKnockout.add(sortedTeams.get(0).getKey());
                teamsMovingOntoKnockout.add(sortedTeams.get(1).getKey());
            }
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
    public List<Team> getNextStageTeams() {
        return null;
    }
    public List<List<Team>> getGroups(){
        return groups;
    }

}