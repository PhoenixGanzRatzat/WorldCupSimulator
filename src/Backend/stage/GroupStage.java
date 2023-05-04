package Backend.stage;
import Backend.Match;
import Backend.Team;

import java.util.*;

public class GroupStage extends Stage{
    List<List<Team>> groups;
    private List<Team> teams;
    private List<Match> matches;
    public GroupStage(List<Team> teams) {
        super(teams);
        this.teams = getTeams();
        this.matches = getMatches();
    }

    @Override
    public void arrangeMatches() {
        //int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
        Collections.sort(teams);
        Collections.reverse(teams);
        ArrayList<Team> temp = new ArrayList<>(teams);
        groups = new ArrayList<>();
        int groupsize = temp.size()/8;
        for (int i = 0; i < 8; i++) {
            ArrayList<Team> group = new ArrayList<>();
            group.add(temp.get(i));
            for(int j = 0; j < groupsize-1; j++){
                int newTeam = (int)Math.floor(Math.random() * (temp.size() - 8) + 8);
                group.add(temp.get(newTeam));
                temp.remove(newTeam);
            }
            groups.add(group);
        }
        for (List<Team> g:groups){
            for(Team home:g){
                for(int i = g.indexOf(home)+1; i < 4; i++){
                    matches.add(new Match(home, g.get(i)));
                }
            }
        }
    }
    public ArrayList<Team> qualified(){
        ArrayList<Team> qualifiedTeams = new ArrayList<>();
        Collections.sort(teams);
        Collections.reverse(teams);
        for(int i = 0; i < 16; i++){
            qualifiedTeams.add(teams.get(i));
        }
        return qualifiedTeams;
    }
    public List<List<Team>> getGroups(){
        return groups;
    }

}