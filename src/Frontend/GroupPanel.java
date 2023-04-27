package Frontend;

import Backend.Match;
import Backend.Team;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The group stage panel
 */
public class GroupPanel extends JPanel implements StagePanel {
    /* SIMULATES */
    // TODO: stand-in for displaying flags
    BufferedImage[] flags = new BufferedImage[211];

    // TODO: Row panels should always be position 1 2 3 4 and the countries information should change which row based on position

    /* __FIELD VARIABLES__ */
    private JTextField roundNumberTextField;
    private int currentRound;
    private Match[] matches;
    private HashMap<Integer, ArrayList<Match>> groupMatches;
    private HashMap<Integer, ArrayList<Team>> groupTeams;
    private HashMap<Team, Integer> teamGroups;

    public static void main(String[] args) {
        GroupPanel panel = new GroupPanel();
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1800, 800);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public GroupPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0, 0, 75));
        teamGroups = new HashMap<>();
        groupMatches = new HashMap<>();
        groupTeams = new HashMap<>();
        groupMatches.put(1, new ArrayList<>());
        groupTeams.put(1, new ArrayList<>());
        currentRound = 1;
        roundNumberTextField = new JTextField("1");
        initPanel();
    }

    @Override
    public boolean checkIfCompleted() {
        return false;
    }

    @Override
    public void initPanel() {
        /* Top bar across window for displaying current round */
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.GREEN);
        /* Center container for group results and group teams */
        JPanel displayPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        displayPanel.setBackground(Color.CYAN);
        /* Bottom bar across window for housing functions */
        JPanel functionPanel = new JPanel();
        functionPanel.setBackground(Color.magenta);

        /* Information Panel */
        JLabel infoLabel = new JLabel("Group Stage!  Round #");
        infoPanel.add(infoLabel);
        infoPanel.add(roundNumberTextField);

        /* __ Display Panel */
        // Container for all group panes - displays each teams w/d/l record and points
        JPanel groupDisplayPanel = new JPanel(new GridLayout(4, 2, 2, 2));
        for (int i = 0; i < 8; i++) {
            groupDisplayPanel.add(createGroupPanel(i));
        }

        // TODO: REMOVE - Testing
        String[] testArgs = new String[]{"United States", "3", "0", "0", "9001"};
        fillGroupPanelRow((JPanel)groupDisplayPanel.getComponent(0), 1, testArgs);
        // TODO: REMOVE - End

        // results sidepane - displays score and outcome between each match in the group
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setPreferredSize(new Dimension(250, 200));

        resultsPanel.add(new JLabel("__Group Results__"));
        for (int c = 0; c < 4; c++) {
            resultsPanel.add(createMatchResultRowPanel());
        }

        //TODO: REMOVE - Testing
        Team team1 = new Team("United States", "USA", null, 0);
        Team team2 = new Team("Canada", "CAN", null, 0);
        Match match44 = new Match(team1, team2, 2, 1);
        fillMatchResultPanel((JPanel) resultsPanel.getComponent(1), match44);
        //TODO: END REMOVE

        // compose display panel
        displayPanel.add(groupDisplayPanel);
        displayPanel.add(resultsPanel);

        /* Function Panel */
        JButton nextRoundBTN = new JButton("Next Round");
        JButton completeStageBTN = new JButton("Complete Stage");
        functionPanel.add(nextRoundBTN);
        functionPanel.add(completeStageBTN);

        this.add(infoPanel, BorderLayout.NORTH);
        this.add(displayPanel, BorderLayout.CENTER);
        this.add(functionPanel, BorderLayout.SOUTH);
    }

    /**
     * Used to ensure that group panels always display row information based on descending position 1 - 4 top to bottom
     */
    private void rearrangeRowPanels() {

    }


    private void simulateStage() {

    }

    private void simulateRound() {

    }

    private void updateDisplayAfterRound() {

    }

    /**
     * This method creates a blank group results row panel that will be used to display
     * match results between teams in a group.
     *
     * @return
     */
    private JPanel createMatchResultRowPanel() {
        JPanel base = new JPanel(new GridLayout(1, 7, 2, 2));
        for (int i = 0; i < 7; i++) {
            base.add(new JLabel("x"));
        }
        return base;
    }

    /**
     * This method takes 1 match and translates it into a row of the group results panel.
     * The row contains information about the teams, scores, and results(WIN/LOSS/DRAW)
     *
     * @param resultsRowPanel - The row panel to populate with info
     * @param match           - The match to translate into a row panel
     */
    private void fillMatchResultPanel(JPanel resultsRowPanel, Match match) {
        /*  Row panel layout
            Country 1 Abbv - Result1 - Score1 "-" Score2 Result2 Country 2 Abbv
        */
        Component[] rowPanelLabels = resultsRowPanel.getComponents();

        // Initialize
        String name1 = match.getTeamOne().getAbbv();
        String name2 = match.getTeamTwo().getAbbv();
        int score1 = match.getTeamOneScore();
        int score2 = match.getTeamTwoScore();
        String result1;
        String result2;
        if (score1 > score2) {
            result1 = "Win";
            result2 = "Loss";
        } else if (score1 < score2) {
            result1 = "Loss";
            result2 = "Win";
        } else {
            result1 = "Draw";
            result2 = "Draw";
        }

        // Compose
        ((JLabel) rowPanelLabels[0]).setText(name1);
        ((JLabel) rowPanelLabels[1]).setText(result1);
        ((JLabel) rowPanelLabels[2]).setText(String.valueOf(score1));
        ((JLabel) rowPanelLabels[3]).setText("-");
        ((JLabel) rowPanelLabels[4]).setText(String.valueOf(score2));
        ((JLabel) rowPanelLabels[5]).setText(result2);
        ((JLabel) rowPanelLabels[6]).setText(name2);
    }

    /**
     * Using List of matches, separate each team and the matches they played out into groups of 4 teams
     * and 6 matches. total of 8 groups.
     */
    private void createGroups() {
        /* put teams into groups */
        for (Match match : matches) {
            // get teams
            Team team1 = match.getTeamOne();
            Team team2 = match.getTeamTwo();
            int groupNumber;

            /* Build match, group, and team, associations */
            if (teamGroups.containsKey(team1)) {         // __ is team1 in a group? __
                groupNumber = teamGroups.get(team1);        // get team1 group number
                groupMatches.get(groupNumber).add(match);   // save match to groupNumber's matches
                groupTeams.get(groupNumber).add(team2);     // add team2 to team1's group
                teamGroups.put(team2, groupNumber);         // associate team2 to group
            } else if (teamGroups.containsKey(team2)) {  // __ is team2 in a group? __
                groupNumber = teamGroups.get(team2);        // get team2 group number
                groupMatches.get(groupNumber).add(match);   // save match to groupNumber's matches
                groupTeams.get(groupNumber).add(team1);     // add team1 to team2's group
                teamGroups.put(team1, groupNumber);         // associate team1 to group
            } else {
                for (Integer gn : groupTeams.keySet()) {     // Search each group for empty group
                    if (groupTeams.get(gn).size() == 0) {    // if group is empty
                        groupTeams.get(gn).add(team2);      // add team1 to group
                        groupTeams.get(gn).add(team1);      // add team2 to group
                        groupMatches.get(gn).add(match);    // add match to group
                    }
                }
            }

        }// END FOR EACH MATCH
    } // END METHOD


    /**
     * Creates the panel that holds the { win, loss, tie, points } data for each group
     *
     * @param groupNumber
     * @return
     */
    private JPanel createGroupPanel(int groupNumber) {
        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));

        base.setBackground(Color.YELLOW);
        base.setPreferredSize(new Dimension(350, 150));
        base.setBorder(new LineBorder(Color.green));
        String groupLetter = String.valueOf((char) (groupNumber + 65)); // '65' = 'A' // TODO: maybe a String
        JLabel groupLabel = new JLabel(groupLetter);
        JPanel titlePane = new JPanel();
        titlePane.setLayout(new GridLayout(1, 6, 2, 2));
        JPanel[] rowPanes = new JPanel[4];

        groupLabel.setBorder(new LineBorder(Color.green));
        // The top of the panel has the group letter displayed
        base.add(groupLabel);

        /*
            __ Title pane __
            Position - Country Name - Wins - Draws - Losses - Points
        */
        JLabel[] titles = new JLabel[6];
        titles[0] = new JLabel("Position");
        titles[1] = new JLabel("Country");
        titles[2] = new JLabel("Wins");
        titles[3] = new JLabel("Draws");
        titles[4] = new JLabel("Losses");
        titles[5] = new JLabel("Points");
        for (JLabel l : titles) {
            l.setHorizontalAlignment(SwingConstants.CENTER);
            titlePane.add(l);
        }
        // next row for group panel is the title panel
        base.add(titlePane);

        // last is the 4 row panes
        for (int i = 0; i < 4; i++) {
            // initialize
            rowPanes[i] = new JPanel();
            rowPanes[i].setLayout(new GridLayout(1, 6, 2, 2));
            rowPanes[i].setFont(new Font("Arial", Font.BOLD, 10));

            // compose
            for (int j = 0; j < 6; j++) {
                JLabel label = new JLabel("*");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanes[i].add(label);
            }
            base.add(rowPanes[i]);
        }
        return base;
    }


    /**
     * Updates the group panel information for the given position/row with the data
     * in args.
     * args = { CountryName, Wins, Draws, Losses, Points }
     *
     * @param groupPanel - Group that is being updated
     * @param position   - selected row panel to update
     * @param args       - data used to update row panel
     */
    private void fillGroupPanelRow(JPanel groupPanel, int position, String[] args) {
        JPanel rowPanel = ((JPanel) groupPanel.getComponents()[position + 1]); // pos+1 gets intended row
        Component[] labels = rowPanel.getComponents();

        ((JLabel) labels[0]).setText(String.valueOf(position));
        for (int i = 1; i < 6; i++) {
            ((JLabel) labels[i]).setText(args[i-1]);
        }

    }
}
