package Frontend;

import Backend.Match;
import Backend.Region;
import Backend.Team;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;

public class MonthPanelTester {

    private static Region northamerica = Region.CONCACAF;
    private static Team team1 = new Team("United States", "USA", null, 0);
    private static Team team2 = new Team("Canada", "CAN", null, 0);
    private static Team team3 = new Team("Mexico", "MEX", null, 8);

    public static void main(String[] args) {
        JFrame frame = new JFrame("Month Panel Testing");
        frame.setSize(new Dimension(1000, 800));
        Match m = new Match(team1, team2, LocalDate.of(2018, 4, 1));
        Match m2 = new Match(team1, team2, LocalDate.of(2018, 4, 5));
        Match m3 = new Match(team1, team2, LocalDate.of(2018, 4, 10));
        Match m4 = new Match(team1, team3, LocalDate.of(2018, 4, 10));
        MonthPanel monthPanel = new MonthPanel(2018, 4, Arrays.asList(m, m2, m3, m4));
        frame.add(monthPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
