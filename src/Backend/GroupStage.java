package Backend;
import java.util.*;

public class GroupStage extends Stage{
    public GroupStage(ArrayList<Team> teams) {
        super(teams);
    }

    @Override
    public void arrangeMatches() {
        //int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
        Collections.sort(teams);
        Collections.reverse(teams);
        ArrayList<ArrayList<Team>> groups = new ArrayList<>();
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
                for(Team away:g){
                    if(home != away){
                        matches.add(new Match(home, away));
                    }
                }
            }
        }
    }
}