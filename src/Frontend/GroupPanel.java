package Frontend;

import Backend.Match;
import Backend.Team;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * The group stage panel
 *
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
    private HashMap<Integer, Match[]> groupMatches;
    private HashMap<Integer, Team[]> groupTeams;
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
        groupMatches.put(1, new Match[6]);
        groupTeams.put(1, new Team[4]);
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
        JPanel groupDisplayPanel = new JPanel(new GridLayout(4,2,2,2));
        for(int i = 0; i < 8; i++) {
            groupDisplayPanel.add(createGroupPanel(i));
        }
        // results sidepane - displays score and outcome between each match in the group
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setPreferredSize(new Dimension(250, 200));

        resultsPanel.add(new JLabel("__Group Results__"));
        for(int c = 0; c < 4; c++) {
            resultsPanel.add(createMatchResultRowPanel());
        }

        //TODO: REMOVE - Testing
        Team team1 = new Team("United States", "USA", null, 0);
        Team team2 = new Team("Canada", "CAN", null, 0);
        Match match44 = new Match(team1, team2, 2, 1);
        fillMatchResultPanel((JPanel)resultsPanel.getComponent(1), match44);
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

    private JPanel createMatchResultRowPanel() {
        JPanel base = new JPanel(new GridLayout(1,7,2,2));
        for(int i = 0; i < 7; i++) {
            base.add(new JLabel("x"));
        }
        return base;
    }

    private void fillMatchResultPanel(JPanel groupPanel, Match match) {
        Component[] rowPanelLabels = groupPanel.getComponents();

        String name1 = match.getTeam1().getAbbv();
        String name2 = match.getTeam2().getAbbv();
        int score1 = match.getTeam1Score();
        int score2 = match.getTeam2Score();
        String result1;
        String result2;
        if(score1 > score2) {
            result1 = "Win";
            result2 = "Loss";
        } else if (score1 < score2) {
            result1 = "Loss";
            result2 = "Win";
        } else {
            result1 = "Draw";
            result2 = "Draw";
        }

        ((JLabel)rowPanelLabels[0]).setText(name1);
        ((JLabel)rowPanelLabels[1]).setText(result1);
        ((JLabel)rowPanelLabels[2]).setText(String.valueOf(score1));
        ((JLabel)rowPanelLabels[3]).setText("-");
        ((JLabel)rowPanelLabels[4]).setText(String.valueOf(score2));
        ((JLabel)rowPanelLabels[5]).setText(result2);
        ((JLabel)rowPanelLabels[6]).setText(name2);
    }

    private void createGroups() {
        /* put teams into groups */
        for(Match match : matches) {
            // get teams
            Team team1 = match.getTeam1();
            Team team2 = match.getTeam2();

            // is team1 in a group?
            if(teamGroups.containsKey(team1)) {
                // add team2 to same group
                int g = teamGroups.get(team1);
                for(int d = 0; d < 4; d++) {
                    if(groupTeams.get(g)[d].equals(null)) {
                        groupTeams.get(g)[d] = team2;
                    }
                }
            } else if(teamGroups.containsKey(team2)) { // is team2 in a group?
                // add team1 to same group
                int g = teamGroups.get(team2);
                for(int d = 0; d < 4; d++) {
                    if(groupTeams.get(g)[d].equals(null)) {
                        groupTeams.get(g)[d] = team1;
                    }
                }
            } else {
                // find empty group
                // add team1 and team2 to empty group
                for(Integer gn : groupTeams.keySet()) {
                    if(groupTeams.get(gn).length == 0) {
                        teamGroups.put(team1, gn);
                        teamGroups.put(team2, gn);
                    }
                }
            }

            /* Put matches into groups */
            for(Match match2 : matches) {
                int groupNum = teamGroups.get(match2.getTeam1());
                Match[] matches = groupMatches.get(groupNum);
                for(int e = 0; e < 6; e++) {
                    if(matches[e].equals(null)) {
                        matches[e] = match2;
                    }
                }
            }

        }// END FOR EACH MATCH
    } // END METHOD


    /**
     * Creates the panel that holds the { win, loss, tie, points } data for each group
     * @param groupNumber
     * @return
     */
    private JPanel createGroupPanel(int groupNumber) {
        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));

        base.setBackground(Color.YELLOW);
        base.setPreferredSize(new Dimension(350, 150));
        base.setBorder(new LineBorder(Color.green));
        String groupLetter = String.valueOf((char)(groupNumber + 65)); // '65' = 'A' // TODO: maybe a String
        JLabel groupLabel = new JLabel(groupLetter);
        JPanel titlePane = new JPanel();
        titlePane.setLayout(new GridLayout(1,6,2,2));
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
        for(JLabel l : titles) {
            l.setHorizontalAlignment(SwingConstants.CENTER);
            titlePane.add(l);
        }
        // next row for group panel is the title panel
        base.add(titlePane);

        // last is the 4 row panes
        for(int i = 0; i < 4; i++) {
            // initialize
            rowPanes[i] = new JPanel();
            rowPanes[i].setLayout(new GridLayout(1,6,2,2));
            rowPanes[i].setFont(new Font("Arial", Font.BOLD,10));

            // compose
            for(int j = 0; j < 6;j++) {
                JLabel label = new JLabel("*");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                rowPanes[i].add(label);
            }
            base.add(rowPanes[i]);
        }

        return base;
    }


    private void fillGroupPanelRow(JPanel groupPanel, int position) {
        // Base (Components)
        // [0]  group label
        // [1]  title pane
        // [2]  1st place row pane
        // [3]  2nd place row pane
        // [4]  3rd place row pane
        // [5]  4th place row pane
    }
}
