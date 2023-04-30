package Backend;

import java.util.*;

public class GroupStage extends Stage{
    public GroupStage(ArrayList<Team> teams) {
        super(teams);
    }

    @Override
    public void arrangeMatches() {
        for(Team t:teams){
            System.out.print(t.getRank() + " ");
        }
        System.out.println("");
        Collections.sort(teams);
        for(Team t:teams){
            System.out.print(t.getRank() + " ");
        }
    }
}
