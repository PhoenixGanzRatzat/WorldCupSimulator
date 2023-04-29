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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
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
    private int[] currentRound;
    private Match[] matches;
    private Team[] teams;
    private HashMap<Integer, ArrayList<Match>> groupMatches;
    private HashMap<Integer, ArrayList<Team>> groupTeams;
    private HashMap<Team, Integer> teamGroups;
    private HashMap<Integer, Match[]> groupSortedMatchesByRound;
    private String selectedGroup;
    private boolean stageComplete;
    private JPanel resultsPanel; // TODO: move panels that I have to dig up into field variables, prevents changes in design from disrupting functionality
    private JPanel groupDisplayPanel;
    private HashMap<String, Image> flags;

    /* TEMPORARY */
    // TODO: Update info panel with current round
    // TODO: it's possible to be on different rounds in different groups, how will this be managed.
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
        Team team1 = new Team("United States", "USA", null, 0);
        Team team2 = new Team("Canada", "CAN", null, 0);
        Team team3 = new Team("Germany", "GER", null, 0);
        Team team4 = new Team("Sudan", "SDN", null, 0);

        Team team5 = new Team("France", "FRA", null, 0);
        Team team6 = new Team("Iran", "IRN", null, 0);
        Team team7 = new Team("Wales", "WAL", null, 0);
        Team team8 = new Team("England", "ENG", null, 0);

        Team team9 = new Team("Netherlands", "NED", null, 0);
        Team team10 = new Team("Ecuador", "ECU", null, 0);
        Team team11 = new Team("Qatar", "QAT", null, 0);
        Team team12 = new Team("Australia", "AUS", null, 0);

        Team team13 = new Team("Croatia", "CRC", null, 0);
        Team team14 = new Team("Japan", "JPN", null, 0);
        Team team15 = new Team("Denmark", "DEN", null, 0);
        Team team16 = new Team("Tunisia", "TUN", null, 0);
        this.teams = new Team[] {team1, team2, team3, team4, team5, team6, team7, team8, team9, team10, team11, team12, team13, team14, team15, team16};

        Match match6 = new Match(team3, team4, 0,1, LocalDate.of(2020, 5, 1));
        Match match5 = new Match(team2, team4, 1,0, LocalDate.of(2020, 4, 1));
        Match match2 = new Match(team1, team3, 1,1, LocalDate.of(2020, 2, 1));
        Match match3 = new Match(team1, team4, 2,1, LocalDate.of(2020, 6, 1));
        Match match4 = new Match(team2, team3, 1,2, LocalDate.of(2020, 3, 1));
        Match match1 = new Match(team1, team2, 2,1, LocalDate.of(2020, 1, 1));

        Match match7 = new Match(team7, team8, 1,1, LocalDate.of(2020, 5, 1));
        Match match8 = new Match(team6, team8, 2,0, LocalDate.of(2020, 4, 1));
        Match match9 = new Match(team5, team7, 1,2, LocalDate.of(2020, 2, 1));
        Match match10 = new Match(team5, team8, 2,2, LocalDate.of(2020, 6, 1));
        Match match11 = new Match(team6, team7, 1,0, LocalDate.of(2020, 3, 1));
        Match match12 = new Match(team5, team6, 1,1, LocalDate.of(2020, 1, 1));

        Match match13 = new Match(team11, team12, 2,1, LocalDate.of(2020, 5, 1));
        Match match14 = new Match(team10, team12, 2,0, LocalDate.of(2020, 4, 1));
        Match match15 = new Match(team9, team11, 0,1, LocalDate.of(2020, 2, 1));
        Match match16 = new Match(team9, team12, 1,1, LocalDate.of(2020, 6, 1));
        Match match17 = new Match(team10, team11, 3,2, LocalDate.of(2020, 3, 1));
        Match match18 = new Match(team9, team10, 1,1, LocalDate.of(2020, 1, 1));

        Match match19 = new Match(team15, team16, 0,2, LocalDate.of(2020, 5, 1));
        Match match20 = new Match(team14, team16, 1,1, LocalDate.of(2020, 4, 1));
        Match match21 = new Match(team13, team15, 1,2, LocalDate.of(2020, 2, 1));
        Match match22 = new Match(team13, team16, 2,0, LocalDate.of(2020, 6, 1));
        Match match23 = new Match(team14, team15, 1,0, LocalDate.of(2020, 3, 1));
        Match match24 = new Match(team13, team14, 2,2, LocalDate.of(2020, 1, 1));

        this.matches = new Match[]{match1, match2, match3, match4, match5, match6,
        match7, match8, match9, match10, match11, match12, match13, match14, match15,
        match16, match17, match18, match19, match20, match21, match22, match23, match24};
        createGroups();
        groupSortedMatchesByRound = organizeMatchesIntoRoundsByGroupNumber();
    }

    /* CONSTRUCTORS */
    public GroupPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0, 0, 75));
        teamGroups = new HashMap<>();
        groupMatches = new HashMap<>();
        groupTeams = new HashMap<>();
        groupMatches.put(1, new ArrayList<>());
        // groupTeams.put(1, new ArrayList<>());
        currentRound = new int[8]; // tracks current round for each group
        roundNumberTextField = new JLabel(String.valueOf(currentRound));
        groupDisplayPanel = new JPanel();
        selectedGroup = "A";
        groupSortedMatchesByRound = new HashMap<>();
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
     * @return state of completion
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
        groupDisplayPanel.setLayout(new GridLayout(4, 2, 2, 2));
        for (int i = 0; i < 8; i++) {
            groupDisplayPanel.add(createGroupPanel(i));
        }
        initGroupPanelRows();

        // results side-pane - displays score and outcome between each match in the group
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setPreferredSize(new Dimension(200, 200));

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
        Component[] rowPanels = groupPanel.getComponents();
        int numRowPanels = rowPanels.length;

        // extract an array of only the panels that need sorting
        JPanel[] panelsToSort = new JPanel[numRowPanels - 2];
        for (int i = 2; i < numRowPanels; i++) {
            panelsToSort[i-2] = (JPanel) rowPanels[i];
        }

        // sort those panels by "point" values in descending order
        Arrays.sort(panelsToSort, (p1, p2) -> {
            int points1 = Integer.parseInt(((JLabel) p1.getComponent(5)).getText());
            int points2 = Integer.parseInt(((JLabel) p2.getComponent(5)).getText());
            return Integer.compare(points2, points1);
        });

        // remove each panel and add them back in the sorted order
        for (int i = 2; i < numRowPanels; i++) {
            ((JLabel) panelsToSort[i-2].getComponent(0)).setText(String.valueOf(i-1));
            groupPanel.remove(panelsToSort[i-2]);
            groupPanel.add(panelsToSort[i-2], i);
        }

        // update the { position } for each panel
        for(int i = 2; i < numRowPanels; i++) {

        }

        // refreshes the display with changes
        groupPanel.revalidate();
        groupPanel.repaint();
    }

    /**
     * When button is pressed to simulate entire stage this method processes the updates to UI and
     * calling for matches to be simulated.
     */
    private void simulateStage() {
        for(int i = 0; i < 6; i++) {
            simulateRound();
        }
        this.stageComplete = true;
    }

    private void simulateRound() {
        // TODO: save this for actual implementation with all matches
         for(int i = 0; i < this.groupTeams.size(); i++) {
            simulateGroupRound(i+1);
         }
        // TODO: testing version
        //simulateGroupRound(1);
    }

    /**
     * When button is pressed to simulate 1 round of matches for a single group, this method calls for those
     * matches to be simulated and updates the UI.
     */
    private void simulateGroupRound(int groupNumber) {
        if(this.currentRound[groupNumber-1] < 6) {
            Match match = groupSortedMatchesByRound.get(groupNumber)[this.currentRound[groupNumber-1]];
            updateGroupPanelInfo(groupNumber, match);
            if(groupNumber == (this.selectedGroup.charAt(0)-64)) {
                fillMatchResultPanel(match, this.currentRound[groupNumber - 1] + 1);// +1 gets index of row panel for that round
            }
            rearrangeRowPanels((JPanel) groupDisplayPanel.getComponent(groupNumber-1));
            this.currentRound[groupNumber-1]++;
        } else {
            // STAGE COMPLETE
            this.stageComplete = true;
        }
    }

    private void initGroupPanelRows() {
        Component[] groupPanels = groupDisplayPanel.getComponents();

        // for each group
        for(int a = 0; a < this.groupTeams.size(); a++) {
            JPanel groupPanel = (JPanel) groupPanels[a];
            Component[] groupRowPanels = groupPanel.getComponents();
            // TODO: FULL IMPLEMENTATION: SAVE THIS
            // ArrayList<Team> groupTeams = this.groupTeams.get(a);
            // TODO: testing
            //ArrayList<Team> groupTeams = this.groupTeams.get(1);

            // groupRowPanels.getComponent(0) = { selection button }
            // groupRowPanels.getComponent(1) = { Title pane }
            // groupRowPanels.getComponent(2 - 5) = { Row panel }
            for(int b = 2; b < 6; b++) {
                // __Component[] casting__
                // A = Component(1) of row panel is a JLabel for "Country Name"
                // B = groupRowPanels[2] = JPanel to hold JLabels for the first row of Country stats
                String str = this.groupTeams.get(a+1).get(b-2).getName();
                // {  A      {   B                      }}
                   ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(1)).setText(str);
                // labels[2-5] set all values to 0
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(2)).setText(String.valueOf(0));
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(3)).setText(String.valueOf(0));
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(4)).setText(String.valueOf(0));
                ((JLabel) ((JPanel) groupRowPanels[b]).getComponent(5)).setText(String.valueOf(0));
            }
        }
    }

    /**
     *
     */
    private void updateGroupPanelInfo(int groupNumber, Match match) {
        JPanel groupPanel = (JPanel) groupDisplayPanel.getComponent(groupNumber - 1);

        Team team1 = match.getTeamOne();
        Team team2 = match.getTeamTwo();
        int t1Score = match.getTeamOneScore();
        int t2Score = match.getTeamTwoScore();

        JPanel country1 = getCountryGroupRowPanel(groupPanel, team1.getName());
        JPanel country2 = getCountryGroupRowPanel(groupPanel, team2.getName());

        /*  index values
            [0] "Position"
            [1] "Country Name" // TODO: MAYBE CHANGE TO ABBV
            [2] "Wins"
            [3] "Draws"
            [4] "Losses"
            [5] "Points"
         */

        if(t1Score > t2Score) {
            updateRowPanelValue(country1, 2, 1); // [2] = wins
            updateRowPanelValue(country1, 5, 3); // [5] = points
            updateRowPanelValue(country2, 4, 1); // [4] = losses
        } else if(t1Score < t2Score) {
            updateRowPanelValue(country2, 2, 1); // [2] = wins
            updateRowPanelValue(country2, 5, 3); // [5] = points
            updateRowPanelValue(country1, 4, 1); // [4] = losses
        } else { // draw
            updateRowPanelValue(country1, 3, 1); // [1] = Draws
            updateRowPanelValue(country1, 5, 1); // [5] = points
            updateRowPanelValue(country2, 3, 1); // [3] = Draws
            updateRowPanelValue(country2, 5, 1); // [5] = points
        }


    }

    /**
     * helper method for updateGroupPanelInfo()
     * @param rowPanel - panel containing labels to update
     * @param index - of label to update
     * @param increment - value to increment jlabel index value by
     */
    private void updateRowPanelValue(JPanel rowPanel, int index, int increment) {
        String indexStr = ((JLabel) rowPanel.getComponent(index)).getText();
        int value = (Integer.parseInt(indexStr)) + increment;
        ((JLabel) rowPanel.getComponent(index)).setText(String.valueOf(value));
    }

    private JPanel getCountryGroupRowPanel(JPanel groupPanel, String country) {
        for(Component row : groupPanel.getComponents()) {
            if(row instanceof JPanel) {
                if( ((JLabel)   ((JPanel)row).getComponent(1)).getText().equals(country)   )  {
                    return ( (JPanel) row);
                }
            }
        }

        return null;
    }

    /**
     * Initialize roundGroupMatches
     *  Round > groupMatches : using .get(roundNumber) to retrieve a Map between groups and matches played in that round.
     *        > group > Matches : used to pull matches played based on group number
     * Each group plays 6 matches so roundGroupMatches.get( 1 - 6 ) will be valid calls
     *
     */
    private HashMap<Integer, Match[]> organizeMatchesIntoRoundsByGroupNumber() {
        // how are matches dated for the group stage? How can I tell which match was part of which round?
        // matches are played chronologically 1 at a time for each group, so chrono order where each game is a round
        HashMap<Integer, Match[]> sortedMatches = new HashMap<>();

        // for each group in groupMatches, organize matches into chronological order
        for (Integer groupNumber : groupMatches.keySet()) {
            ArrayList<Match> matches = groupMatches.get(groupNumber);
            Match[] sorted = new Match[6];
            int i = 0;

            // sort the matches chronologically by date
            // matches.sort(Comparator.comparing(Match::getMatchDate));

            // add the sorted matches to the array
            for (Match match : matches) {
                sorted[i] = match;
                i++;
            }

            // add the sorted array to the hashmap with the group number as the key
            sortedMatches.put(groupNumber, sorted);
        }

        return sortedMatches;
    }


    /**
     * This method creates a blank group results row panel that will be used to display
     * match results between teams in a group.
     *
     * @return 1 empty row for the match results pane
     */
    private JPanel createMatchResultRowPanel() {
        JPanel base = new JPanel(new FlowLayout());
        for (int i = 0; i < 7; i++) {
            if(i == 3) {
                base.add(new JLabel("-"));
            } else {
                base.add(new JLabel("x"));
            }
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
                if(!groupTeams.get(groupNumber).contains(team2)) {  // if group doesn't contain team 2
                    groupTeams.get(groupNumber).add(team2);         // add team2 to team1's group
                    teamGroups.put(team2, groupNumber);             // associate team2 to group
                }
            } else if (teamGroups.containsKey(team2)) {  // __ is team2 in a group? __
                groupNumber = teamGroups.get(team2);        // get team2 group number
                groupMatches.get(groupNumber).add(match);   // save match to groupNumber's matches
                if(!groupTeams.get(groupNumber).contains(team1)) {  // if group doesn't contain team 2
                    groupTeams.get(groupNumber).add(team1);         // add team1 to team2's group
                    teamGroups.put(team1, groupNumber);             // associate team1 to group
                }
            } else {
                // if < 8 groups then add new group
                if(groupTeams.size() < 8) {
                    int size = groupTeams.size();
                    groupTeams.put((size + 1), new ArrayList<>());
                    groupMatches.put((size + 1), new ArrayList<>());

                }

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

        // sort the matches chronologically by date
        for(Integer i : groupMatches.keySet()) {
            groupMatches.get(i).sort(Comparator.comparing(Match::getMatchDate));
        }


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
        base.setPreferredSize(new Dimension(500, 150));
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
            rowPanes[i].setLayout(new GridLayout(1,6,2,2));
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
     * Called by select group button on top of each group panel. This method takes the matches
     * from the selected group and displays them on the side-pane results panel.
     * @param selectedGroup - use matches from this group
     */
    private void changeSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
        int groupNum = this.selectedGroup.charAt(0)-64;
        // update results panel up to current round
        backPopulateResultsPanel(groupNum, this.currentRound[groupNum-1]);

        // update function button
        Component[] rootChildren = this.getComponents();
        JPanel functionPanel = (JPanel) rootChildren[2];
        Component[] functionPanelChildren = functionPanel.getComponents();
        JButton nextGroupRoundBTN = (JButton) functionPanelChildren[0];
        nextGroupRoundBTN.setText("Next round in group " + this.selectedGroup);
    }

    private void backPopulateResultsPanel(int groupNum, int round) {
        clearResultsPanel();
        for(int i = 0; i < round; i++) {
            Match match = groupSortedMatchesByRound.get(groupNum)[i];
            fillMatchResultPanel(match, i+1);
        }

    }

    private void clearResultsPanel() {
        Component[] allRowPanels = this.resultsPanel.getComponents();
        // [0]"Group Results", [1]round 1 match results row panel, [2-5]..., [6]round 6 match results row panel.
        for(int row = 1; row < allRowPanels.length; row++) {
            JPanel roundRowPanel = (JPanel) allRowPanels[row];
            Component[] labels = roundRowPanel.getComponents();

            // Compose
            ((JLabel) labels[0]).setText("x");
            ((JLabel) labels[1]).setText("x");
            ((JLabel) labels[2]).setText("x");
            ((JLabel) labels[3]).setText("-");
            ((JLabel) labels[4]).setText("x");
            ((JLabel) labels[5]).setText("x");
            ((JLabel) labels[6]).setText("x");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton pressed = (JButton) e.getSource();
        String text = pressed.getText();

        // This buttons text changes
        if(text.contains("Next round in group")) {
            text = "Simulate group round";
        }

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
            case "Simulate group round":
                simulateGroupRound(((int) this.selectedGroup.charAt(0)) - 64);
                break;
            case "Next round for all groups":
                simulateRound();
                break;
            case "Complete Stage":
                simulateStage();
                break;

        }
    }

}
