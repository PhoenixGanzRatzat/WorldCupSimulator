package Frontend;

import Backend.Match;
import Backend.Region;
import Backend.Team;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;

public class MonthPanelTester {
/*
    private static Region northamerica = Region.CONCACAF;
    private static Team team1 = new Team("United States", "USA", null, 0);
    private static Team team2 = new Team("Canada", "CAN", null, 0);
    private static Team team3 = new Team("Mexico", "MEX", null, 8);
 */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Month Panel Testing");
        frame.setSize(new Dimension(1000, 800));
        /*
        Match m = new Match(team1, team2, LocalDate.of(2018, 2, 1));
        Match m2 = new Match(team1, team2, LocalDate.of(2018, 2, 5));
        Match m3 = new Match(team1, team2, LocalDate.of(2018, 2, 10));
        Match m4 = new Match(team1, team3, LocalDate.of(2018, 2, 10));
        */
        Team team1 = new Team("United States", "USA", null, 0);
        Team team2 = new Team("Canada", "CAN", null, 0);
        Team team3 = new Team("Germany", "GER", null, 0);
        Team team4 = new Team("Sudan", "SDN", null, 0);

        Team team5 = new Team("France", "FRA", null, 0);
        Team team6 = new Team("Iran", "IRN", null, 0);
        Team team7 = new Team("Wales", "WAL", null, 0);
        Team team8 = new Team("England", "ENG", null, 0);
        Match match6  = new Match(team3 , team4 , 0,1, LocalDate.of(2020, 2, 1));
        Match match5  = new Match(team2 , team4 , 1,0, LocalDate.of(2020, 2, 1));
        Match match2  = new Match(team1 , team3 , 1,1, LocalDate.of(2020, 2, 1));
        Match match3  = new Match(team1 , team4 , 2,1, LocalDate.of(2020, 2, 15));
        Match match4  = new Match(team2 , team3 , 1,2, LocalDate.of(2020, 2, 15));
        Match match1  = new Match(team1 , team2 , 2,1, LocalDate.of(2020, 2, 14));
        Match match7  = new Match(team7 , team8 , 1,1, LocalDate.of(2020, 2, 12));
        Match match8  = new Match(team6 , team8 , 2,0, LocalDate.of(2020, 2, 2));
        Match match9  = new Match(team5 , team7 , 1,2, LocalDate.of(2020, 2, 18));
        Match match10 = new Match(team5 , team8 , 2,2, LocalDate.of(2020, 2, 28));
        Match match11 = new Match(team6 , team7 , 1,0, LocalDate.of(2020, 2, 26));
        Match match12 = new Match(team5 , team6 , 1,1, LocalDate.of(2020, 2, 25));
        // MonthPanel monthPanel = new MonthPanel(2018, 2, Arrays.asList(m, m2, m3, m4));
        MonthPanel monthPanel = new MonthPanel(2020, 2, Arrays.asList(match1, match2, match3, match4,
        match5, match6, match7, match8, match9, match10, match11, match12));
        frame.add(monthPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
