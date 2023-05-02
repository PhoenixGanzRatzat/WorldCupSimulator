package Backend.stage;
import Backend.Match;
import Backend.Team;

import java.util.*;

public class GroupStage extends Stage{
    ArrayList<ArrayList<Team>> groups;
    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    public GroupStage(ArrayList<Team> teams) {
        super(teams);
    }

    @Override
    public void arrangeMatches() {
        //int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
        Collections.sort(teams);
        Collections.reverse(teams);
        groups = new ArrayList<>();
        int groupsize = teams.size()/8;
        for (int i = 0; i < 8; i++) {
            ArrayList<Team> group = new ArrayList<>();
            group.add(teams.get(i));
            for(int j = 0; j < groupsize-1; j++){
                int newTeam = (int)Math.floor(Math.random() * (teams.size() - 8) + 8);
                group.add(teams.get(newTeam));
                teams.remove(newTeam);
            }
            groups.add(group);
        }
        for (ArrayList<Team> g:groups){
            for(Team home:g){
                for(int i = g.indexOf(home) + 1; i < g.size(); i++){
                    matches.add(new Match(home, g.get(i)));
                }
            }
        }
    }
    public ArrayList<ArrayList<Team>> getGroups(){
        return groups;
    }
}