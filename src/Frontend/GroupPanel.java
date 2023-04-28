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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The group stage panel
 */
public class GroupPanel extends JPanel implements StagePanel, ActionListener {
    /* __FIELD VARIABLES__ */
    private JLabel roundNumberTextField;
    private int currentRound;
    private Match[] matches;
    private Team[] teams;
    private HashMap<Integer, ArrayList<Match>> groupMatches;
    private HashMap<Integer, ArrayList<Team>> groupTeams;
    private HashMap<Team, Integer> teamGroups;
    private HashMap<Integer, HashMap<Integer, ArrayList<Match>>> roundGroupMatches;
    private String selectedGroup;
    private boolean stageComplete;
    private JPanel resultsPanel; // TODO: move panels that I have to dig up into field variables, prevents changes in design from disrupting functionality
    private HashMap<String, Image> flags;

    /* TEMPORARY */
    public static void main(String[] args) {
        GroupPanel panel = new GroupPanel();
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1800, 800);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void testMatches() {
        // teams: alpha, bravo, charlie, delta
        /* matches
            alpha v bravo
            alpha v charlie
            alpha v delta
            bravo v charlie
            bravo v delta
            charlie v delta
         */
        Team team1 = new Team("Alpha", "USA", null, 0);
        Team team2 = new Team("Beta", "CAN", null, 0);
        Team team3 = new Team("Charlie", "GER", null, 0);
        Team team4 = new Team("Delta", "SDN", null, 0);
        this.teams = new Team[] {team1, team2, team3, team4};

        Match match1 = new Match(team1, team2, 2,1);
        Match match2 = new Match(team1, team3, 1,1);
        Match match3 = new Match(team1, team4, 2,1);
        Match match4 = new Match(team2, team3, 1,2);
        Match match5 = new Match(team2, team4, 1,0);
        Match match6 = new Match(team3, team4, 0,1);

        Match[] matches = new Match[]{match1, match2, match3, match4, match5, match6};
        this.matches = matches;
        createGroups();
    }

    /* CONSTRUCTORS */
    public GroupPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0, 0, 75));
        teamGroups = new HashMap<>();
        groupMatches = new HashMap<>();
        groupTeams = new HashMap<>();
        groupMatches.put(1, new ArrayList<>());
        groupTeams.put(1, new ArrayList<>());
        currentRound = 1;
        roundNumberTextField = new JLabel(String.valueOf(currentRound));
        selectedGroup = "A";
        resultsPanel = new JPanel();
        flags = new HashMap<>();
        try {
            testMatches();
            initFlags();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initPanel();
    }

    private void initFlags() throws IOException {
        for(Team team : this.teams) {
            String abbv = team.getAbbv();
            BufferedImage flag = ImageIO.read(new File("Assets\\Images\\smallFlags\\" + abbv + ".png"));
            flags.put(abbv, flag);
        }
    }

    /**
     * Returns if the group stage matches have been fully simulated and the group stage
     * is complete.
     * @return
     */
    @Override
    public boolean checkIfCompleted() {
        return stageComplete;
    }

    /**
     * Create an empty group stage panel that will be filled in as the user interacts with function buttons
     */
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

        // results side-pane - displays score and outcome between each match in the group
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setPreferredSize(new Dimension(250, 200));

        resultsPanel.add(new JLabel("__Group Results__"));
        for (int c = 0; c < 6; c++) {
            resultsPanel.add(createMatchResultRowPanel());
        }
        // compose display panel
        displayPanel.add(groupDisplayPanel);
        displayPanel.add(resultsPanel);

        /* Function Panel */
        JButton nextRoundSelectedGroupBTN = new JButton("Next round in group " + selectedGroup);
        nextRoundSelectedGroupBTN.addActionListener(this);
        JButton nextRoundAllGroupsBTN = new JButton("Next round for all groups");
        nextRoundAllGroupsBTN.addActionListener(this);
        JButton completeStageBTN = new JButton("Complete Stage");
        completeStageBTN.addActionListener(this);
        // compose function panel
        functionPanel.add(nextRoundSelectedGroupBTN);
        functionPanel.add(nextRoundAllGroupsBTN);
        functionPanel.add(completeStageBTN);

        // compose root panel
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(displayPanel, BorderLayout.CENTER);
        this.add(functionPanel, BorderLayout.SOUTH);
    }

    /**
     * Used to ensure that group panels always display row information based on descending position 1 - 4 top to bottom
     */
    private void rearrangeRowPanels(JPanel groupPanel) {

    }

    /**
     * When button is pressed to simulate entire stage this method processes the updates to UI and
     * calling for matches to be simulated.
     */
    private void simulateStage() {
        this.stageComplete = true;
    }

    /**
     * When button is pressed to simulate 1 round of matches for each group this method calls for those
     * matches to be simulated and updates the UI.
     */
    private void simulateRound() {
        // 3 points for win, 1 point for tie
    }

    /**
     * When button is pressed to simulate 1 round of matches for a single group this method calls for those
     * matches to be simulated and updates the UI.
     */
    private void simulateGroupRound() {
    }

    private void updateDisplayAfterRound() {

    }

    /**
     * Initialize roundGroupMatches
     *  Round > groupMatches : using .get(roundNumber) to retrieve a Map between groups and matches played in that round.
     *        > group > Matches : used to pull matches played based on group number
     * Each group plays 6 matches so roundGroupMatches.get( 1 - 6 ) will be valid calls
     *
     */
    private void organizeMatchesIntoRoundsByGroupNumber() {
        // how are matches dated for the group stage? How can I tell which match was part of which round?
        // matches are played chronologically 1 at a time for each group, so chrono order where each game is a round
    }

    /**
     * When a group is selected this method is called to update the sideBar match results
     * with the matches from the selected group.
     */
    private void updateMatchResultsPanel(int groupNumber) {
        int index = 1; // TODO: REMOVE
        for(Match match : groupMatches.get(groupNumber)) {
            fillMatchResultPanel(match, index);
            index++;
        }
    }

    /**
     * This method creates a blank group results row panel that will be used to display
     * match results between teams in a group.
     *
     * @return 1 empty row for the match results pane
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
     * @param match - The match to translate into a row panel
     */
    private void fillMatchResultPanel(Match match, int round) {
        /*  Row panel layout
            Country 1 Abbv - Result1 - Score1 "-" Score2 Result2 Country 2 Abbv
        */
        Component[] allRowPanels = this.resultsPanel.getComponents();
        // [0]"Group Results", [1]round 1 match results row panel, [2-5]..., [6]round 6 match results row panel.
        JPanel roundRowPanel = (JPanel) allRowPanels[round];
        Component[] labels = roundRowPanel.getComponents();

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
        ((JLabel) labels[0]).setText(name1);
        ((JLabel) labels[1]).setText(result1);
        ((JLabel) labels[2]).setText(String.valueOf(score1));
        ((JLabel) labels[3]).setText("-");
        ((JLabel) labels[4]).setText(String.valueOf(score2));
        ((JLabel) labels[5]).setText(result2);
        ((JLabel) labels[6]).setText(name2);
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
                        groupTeams.get(gn).add(team1);      // add team1 to group
                        teamGroups.put(team1, gn); // associate team1 to group
                        groupTeams.get(gn).add(team2);      // add team2 to group
                        teamGroups.put(team2, gn); // associate team2 to a group
                        groupMatches.get(gn).add(match);    // add match to group
                    }
                }
            }

        }// END FOR EACH MATCH
    } // END METHOD

    /**
     * Creates the panel that holds the { win, loss, tie, points } data for each group
     *
     * @param groupNumber - used to get group letter
     * @return - constructed empty group panel
     */
    private JPanel createGroupPanel(int groupNumber) {
        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        base.setBackground(Color.YELLOW);
        base.setPreferredSize(new Dimension(350, 150));
        base.setBorder(new LineBorder(Color.green));

        String groupLetter = String.valueOf((char) (groupNumber + 65)); // '65' = 'A' // TODO: maybe a String
        JButton groupSelectBTN = new JButton(groupLetter); // top of each group panel has a 'Select this group' button
        groupSelectBTN.addActionListener(this);

        JPanel titlePane = new JPanel(); // display the Title that goes above each column in group panel
        titlePane.setLayout(new GridLayout(1, 6, 2, 2));

        // The top of the panel has the group letter displayed
        base.add(groupSelectBTN);

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
        JPanel[] rowPanes = new JPanel[4]; // displays team information
        for (int i = 0; i < 4; i++) {
            // initialize
            rowPanes[i] = new JPanel();
            rowPanes[i].setLayout(new GridLayout(1, 6, 2, 2));
            rowPanes[i].setFont(new Font("Arial", Font.BOLD, 10));

            // compose
            for (int j = 0; j < 6; j++) {
                JLabel label = new JLabel("*");
                if(j == 0) {
                    label.setText(String.valueOf(i + 1)); // display position (1-4)
                }
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

    /**
     * Called by select group button on top of each group panel. This method takes the matches
     * from the selected group and displays them on the side-pane results panel.
     * @param selectedGroup - use matches from this group
     */
    private void changeSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
        updateMatchResultsPanel(((int) this.selectedGroup.charAt(0)) - 64); // A = 65

        // update function button
        Component[] rootChildren = this.getComponents();
        JPanel functionPanel = (JPanel) rootChildren[2];
        Component[] functionPanelChildren = functionPanel.getComponents();
        JButton nextGroupRoundBTN = (JButton) functionPanelChildren[0];
        nextGroupRoundBTN.setText("Next round in group " + this.selectedGroup);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressed = (JButton) e.getSource();
        String text = pressed.getText();

        switch(text) {
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
            case "F":
            case "G":
            case "H":
                changeSelectedGroup(text);
                break;
            case "Next round in group":
                simulateGroupRound();
                break;
            case "Next round all groups":
                simulateRound();
                break;
            case "Complete Stage":
                simulateStage();
                break;

        }
    }
}
